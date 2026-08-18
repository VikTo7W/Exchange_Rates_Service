CREATE TABLE IF NOT EXISTS "SettingCalculationRules" (
     "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     "name" TEXT NOT NULL,
     "margin_percentage" NUMERIC(5,4) NOT NULL,
     "rounding_type" ROUNDING_TYPE NOT NULL,
     "source_stat_operation" STATISTICAL_OPERATION NOT NULL
);
