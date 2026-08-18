CREATE TABLE IF NOT EXISTS "FilterValuesPerSetting" (
      "setting_id" INTEGER NOT NULL,
      "filter_value_id" INTEGER NOT NULL,
      PRIMARY KEY ("setting_id", "filter_value_id")
);
