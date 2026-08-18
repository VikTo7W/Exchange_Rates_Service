CREATE TABLE IF NOT EXISTS "CurrencyPairsPerSource" (
      "pair_id" INTEGER NOT NULL,
      "source_id" INTEGER NOT NULL,
      "from_currency_value" TEXT,
      "from_currency_value_type" JSON_VALUE NOT NULL,
      "to_currency_value" TEXT,
      "to_currency_value_type" JSON_VALUE NOT NULL,
      PRIMARY KEY ("pair_id", "source_id")
);
