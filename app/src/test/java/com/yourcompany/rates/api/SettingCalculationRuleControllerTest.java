package com.yourcompany.rates.api;

import com.yourcompany.rates.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class SettingCalculationRuleControllerTest extends PostgresTestContainer {

    private final MockMvc mockMvc;

    SettingCalculationRuleControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void create_test() throws Exception {
        mockMvc.perform(post("/api/admin/settingCalculationRules")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "name": "Main",
                  "marginPercentage": 0.0152,
                  "roundingType": "NEAREST",
                  "sourceStatOperation": "MEAN"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andDo(print());
    }

    @Test
    public void get_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(get("/api/admin/settingCalculationRules/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.name").value("Main"))
            .andDo(print());
    }

    @Test
    public void getAll_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(get("/api/admin/settingCalculationRules"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(2))
            .andDo(print());
    }

    @Test
    public void changeMargin_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/settingCalculationRules/{id}/changeMargin", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newMargin": 0.0134
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.marginPercentage").value(0.0134))
            .andExpect(jsonPath("$.data.name").value("Main"))
            .andDo(print());
    }

    @Test
    public void delete_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(delete("/api/admin/settingCalculationRules/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.name").value("Main"))
            .andDo(print());

        mockMvc.perform(get("/api/admin/settingCalculationRules"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andDo(print());
    }

    private void prepareEnvironment() throws Exception {
        mockMvc.perform(post("/api/admin/settingCalculationRules")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "id": 0,
                  "name": "Main",
                  "marginPercentage": 0.0152,
                  "roundingType": "NEAREST",
                  "sourceStatOperation": "MEAN"
                }
            """));

        mockMvc.perform(post("/api/admin/settingCalculationRules")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "id": 0,
                  "name": "Secondary",
                  "marginPercentage": 0.0173,
                  "roundingType": "NEAREST",
                  "sourceStatOperation": "MEAN"
                }
            """));
    }
}
