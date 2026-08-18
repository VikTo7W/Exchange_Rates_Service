package com.yourcompany.rates.service.worker.util;

import com.jayway.jsonpath.JsonPath;
import com.yourcompany.rates.data.enums.OrderType;
import com.yourcompany.rates.common.exception.HtmlResponseProcessingException;
import com.yourcompany.rates.common.exception.InvalidOrderWindowException;
import com.yourcompany.rates.common.exception.JsonResponseProcessingException;
import com.yourcompany.rates.common.exception.UnimplementedResponseTypeException;
import com.yourcompany.rates.service.worker.dto.CompletedResponse;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Entities;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RateResponseProcessor {

    private List<Double> sourceRates;
    private String body;
    private int fromRate;
    private int toRate;
    private OrderType ordersType;
    private String buyOrdersArrayPath;
    private String sellOrdersArrayPath;
    private String ratePath;
    private String contentType;
    private String sourceName;
    private final Logger log;

    public RateResponseProcessor(CompletedResponse completed) throws IOException {
        this.log = Logger.getLogger(RateResponseProcessor.class.getName());
        this.sourceRates = new ArrayList<>();
        this.body = completed.response().body().string();
        this.fromRate = completed.context().setting().setting().ordersWindowStart();
        this.toRate = completed.context().setting().setting().ordersWindowEnd();
        this.ratePath = completed.context().setting().source().rateKeyPath();
        this.contentType = completed.response().headers().get("Content-Type");
        this.sourceName = completed.context().setting().source().name();
        this.buyOrdersArrayPath = completed.context().setting().source().buyOrdersArrayKeyPath();
        this.sellOrdersArrayPath = completed.context().setting().source().sellOrdersArrayKeyPath();
        this.ordersType = completed.context().setting().setting().ordersType();
    }

    public List<Double> process() {

        if (contentType.contains("json")) {
            try {
                processJson();
            } catch (Exception e) {
                log.severe("Error processing the JSON response: " + e.getMessage());
                throw new JsonResponseProcessingException("Error processing the JSON response", e);
            }
        } else if (contentType.contains("html")) {
            try {
                processHtml();
            } catch (Exception e) {
                log.severe("Error processing the HTML response: " + e.getMessage());
                throw new HtmlResponseProcessingException("Error processing the HTML response", e);
            }
        } else if (contentType.contains("xml")) {
            throw new UnimplementedResponseTypeException(sourceName);
        } else {
            throw new UnimplementedResponseTypeException(sourceName);
        }

        return sourceRates;
    }

    private void processJson() {
        Object ordersObject = ordersType == OrderType.BUY ? JsonPath.read(body, buyOrdersArrayPath) : JsonPath.read(body, sellOrdersArrayPath);
        List<?> rates;

        if (ordersObject instanceof List<?>) {
            rates = (List<?>) ordersObject;
        } else {
            rates = List.of(ordersObject);
        }

        if (rates.isEmpty() || rates.size() < fromRate) {
            throw new InvalidOrderWindowException("Response orders array size is smaller than the specified starting position");
        }

        rates.stream()
            .skip(fromRate - 1)
            .limit(toRate - fromRate + 1)
            .forEach(rateValue -> {
                BigDecimal rate = new BigDecimal(JsonPath.read(rateValue, ratePath).toString());
                sourceRates.add(rate.doubleValue());
            });
    }

    private void processHtml() throws XPathExpressionException {

        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(body);
        htmlDoc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);
        org.w3c.dom.Document xhtmlDoc = new W3CDom().fromJsoup(htmlDoc);

        XPath xpath = XPathFactory.newInstance().newXPath();

        if (buyOrdersArrayPath == null && sellOrdersArrayPath == null) {
            String rate = xpath.evaluate(ratePath, xhtmlDoc);
            sourceRates.add(Double.parseDouble(rate));
            return;
        }

        NodeList rateNodes = ordersType == OrderType.BUY ? (NodeList) xpath.evaluate(
            buyOrdersArrayPath,
            xhtmlDoc,
            XPathConstants.NODESET
        ) : (NodeList) xpath.evaluate(
            sellOrdersArrayPath,
            xhtmlDoc,
            XPathConstants.NODESET
        );

        if (rateNodes.getLength() == 0 || rateNodes.getLength() < fromRate) {
            throw new InvalidOrderWindowException("Response orders array size is smaller than the specified starting position");
        }

        for (int i = fromRate - 1; i < Math.min(toRate, rateNodes.getLength()); i++) {
            Node item = rateNodes.item(i);

            String valueStr = xpath.evaluate(
                ratePath,
                item
            );

            if (!valueStr.isBlank()) {
                sourceRates.add(Double.parseDouble(valueStr));
            }
        }
    }
}
