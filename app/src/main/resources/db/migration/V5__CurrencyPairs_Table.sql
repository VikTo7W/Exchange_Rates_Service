CREATE TABLE IF NOT EXISTS "CurrencyPairs" (
     "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     "from_currency_id" INTEGER NOT NULL,
     "to_currency_id" INTEGER NOT NULL
);
