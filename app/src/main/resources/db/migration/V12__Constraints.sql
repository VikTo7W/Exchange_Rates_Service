ALTER TABLE "SettingCalculationRules"
  ADD CONSTRAINT uq_SCR_name UNIQUE ("name");

ALTER TABLE "RateSources"
  ADD CONSTRAINT uq_RS_name UNIQUE ("name");

ALTER TABLE "Currencies"
  ADD CONSTRAINT uq_Currencies_name UNIQUE ("name");

ALTER TABLE "RateBroadcastingSettings"
  ADD CONSTRAINT uq_RBS_name UNIQUE ("name");

ALTER TABLE "Filters"
  ADD CONSTRAINT uq_Filters_name UNIQUE ("name");

ALTER TABLE "CurrencyPairs"
  ADD CONSTRAINT fk_CP_from_currency
    FOREIGN KEY ("from_currency_id")
      REFERENCES "Currencies"("id")
      ON DELETE CASCADE;

ALTER TABLE "CurrencyPairs"
  ADD CONSTRAINT fk_CP_to_currency
    FOREIGN KEY ("to_currency_id")
      REFERENCES "Currencies"("id")
      ON DELETE CASCADE;

ALTER TABLE "CurrencyPairsPerSource"
  ADD CONSTRAINT fk_CPPS_pair
    FOREIGN KEY ("pair_id")
      REFERENCES "CurrencyPairs"("id")
      ON DELETE CASCADE;

ALTER TABLE "CurrencyPairsPerSource"
  ADD CONSTRAINT fk_CPPS_source
    FOREIGN KEY ("source_id")
      REFERENCES "RateSources"("id")
      ON DELETE CASCADE;

ALTER TABLE "RateBroadcastingSettings"
  ADD CONSTRAINT fk_RBS_source
    FOREIGN KEY ("source_id")
      REFERENCES "RateSources"("id");

ALTER TABLE "RateBroadcastingSettings"
  ADD CONSTRAINT fk_RBS_calculation_rule
    FOREIGN KEY ("calculation_rule_id")
      REFERENCES "SettingCalculationRules"("id");

ALTER TABLE "RateBroadcastingSettings"
  ADD CONSTRAINT fk_RBS_currency_pair
    FOREIGN KEY ("currency_pair_id")
      REFERENCES "CurrencyPairs"("id");

ALTER TABLE "FixRates"
  ADD CONSTRAINT fk_FixRates_setting
    FOREIGN KEY ("setting_id")
      REFERENCES "RateBroadcastingSettings"("id");

ALTER TABLE "Filters"
  ADD CONSTRAINT fk_Filters_source
    FOREIGN KEY ("source_id")
      REFERENCES "RateSources"("id")
      ON DELETE CASCADE;

ALTER TABLE "FiltersValues"
  ADD CONSTRAINT fk_FiltersValues_filter
    FOREIGN KEY ("filter_id")
      REFERENCES "Filters"("id")
      ON DELETE CASCADE;

ALTER TABLE "FiltersValues"
  ADD CONSTRAINT fk_FiltersValues_currency
    FOREIGN KEY ("currency_id")
      REFERENCES "Currencies"("id")
      ON DELETE CASCADE;

ALTER TABLE "FilterValuesPerSetting"
  ADD CONSTRAINT fk_FVPS_setting
    FOREIGN KEY ("setting_id")
      REFERENCES "RateBroadcastingSettings"("id")
      ON DELETE CASCADE;

ALTER TABLE "FilterValuesPerSetting"
  ADD CONSTRAINT fk_FVPS_filter_value
    FOREIGN KEY ("filter_value_id")
      REFERENCES "FiltersValues"("id")
      ON DELETE CASCADE;
