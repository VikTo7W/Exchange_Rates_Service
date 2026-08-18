CREATE TABLE IF NOT EXISTS "FiltersValues" (
     "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     "filter_id" INTEGER NOT NULL,
     "currency_id" INTEGER,
     "name" TEXT NOT NULL,
     "value" TEXT NOT NULL
);
