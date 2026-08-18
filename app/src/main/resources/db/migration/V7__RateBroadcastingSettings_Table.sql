CREATE TABLE IF NOT EXISTS "RateBroadcastingSettings" (
      "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
      "name" TEXT NOT NULL,
      "source_id" INTEGER NOT NULL,
      "calculation_rule_id" INTEGER NOT NULL,
      "currency_pair_id" INTEGER NOT NULL,
      "orders_type" ORDER_TYPE NOT NULL,
      "orders_window_start" INTEGER NOT NULL,
      "orders_window_end" INTEGER NOT NULL,
      "broadcasting_timeout" INTEGER NOT NULL,
      "broadcasting_status" BROADCASTING_STATUS NOT NULL
);
