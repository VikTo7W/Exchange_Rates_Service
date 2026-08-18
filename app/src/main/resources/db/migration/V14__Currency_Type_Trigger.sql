CREATE OR REPLACE FUNCTION trg_validate_currency_pair_types()
  RETURNS TRIGGER AS $$
DECLARE
  from_type CURRENCY_TYPE;
  to_type   CURRENCY_TYPE;
BEGIN
  SELECT "type" INTO from_type
  FROM "Currencies"
  WHERE "id" = NEW."from_currency_id";

  SELECT "type" INTO to_type
  FROM "Currencies"
  WHERE "id" = NEW."to_currency_id";

  IF from_type = to_type THEN
    RAISE EXCEPTION
      'Invalid currency pair: both currencies are of type %',
      from_type
      USING ERRCODE = '23514';
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS validate_currency_pair_types ON "CurrencyPairs";

CREATE TRIGGER validate_currency_pair_types
  BEFORE INSERT OR UPDATE
  ON "CurrencyPairs"
  FOR EACH ROW
EXECUTE FUNCTION trg_validate_currency_pair_types();
