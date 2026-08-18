CREATE OR REPLACE FUNCTION prevent_delete_currency_pair_active()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM "RateBroadcastingSettings"
        WHERE currency_pair_id = OLD.id
          AND broadcasting_status = 'ACTIVE'
    ) THEN
        RAISE EXCEPTION
            'Cannot delete CurrencyPair %, used by active settings',
            OLD.id
            USING ERRCODE = '23514';
END IF;
RETURN OLD;
END;
$$;

CREATE TRIGGER trg_prevent_delete_currency_pair_active
  BEFORE DELETE ON "CurrencyPairs"
  FOR EACH ROW EXECUTE FUNCTION prevent_delete_currency_pair_active();
