CREATE OR REPLACE FUNCTION validate_currency_pair_per_source_values()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
rs RECORD;
BEGIN
SELECT payload_from_currency_path,
       payload_to_currency_path
INTO rs
FROM "RateSources"
WHERE id = NEW.source_id;

IF rs.payload_from_currency_path IS NOT NULL
       AND rs.payload_from_currency_path <> ''
       AND (
            NEW.from_currency_value IS NULL
         OR NEW.from_currency_value = ''
         OR NEW.from_currency_value_type = 'NULL'
       )
    THEN
        RAISE EXCEPTION
            'FROM currency mapping required for source %, but value is missing or NULL',
            NEW.source_id
            USING ERRCODE = '23514';
END IF;

    IF rs.payload_to_currency_path IS NOT NULL
       AND rs.payload_to_currency_path <> ''
       AND (
            NEW.to_currency_value IS NULL
         OR NEW.to_currency_value = ''
         OR NEW.to_currency_value_type = 'NULL'
       )
    THEN
        RAISE EXCEPTION
            'TO currency mapping required for source %, but value is missing or NULL',
            NEW.source_id
            USING ERRCODE = '23514';
END IF;

RETURN NEW;
END;
$$;

CREATE TRIGGER trg_validate_currency_pair_per_source_values
  BEFORE INSERT OR UPDATE
  ON "CurrencyPairsPerSource"
  FOR EACH ROW
EXECUTE FUNCTION validate_currency_pair_per_source_values();
