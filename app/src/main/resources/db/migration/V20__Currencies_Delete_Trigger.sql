CREATE OR REPLACE FUNCTION prevent_delete_currency_active()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM "CurrencyPairs" cp
        JOIN "RateBroadcastingSettings" rbs
          ON rbs.currency_pair_id = cp.id
        WHERE (cp.from_currency_id = OLD.id
            OR cp.to_currency_id = OLD.id)
          AND rbs.broadcasting_status = 'ACTIVE'
    ) THEN
        RAISE EXCEPTION
            'Cannot delete Currency %, used by active settings',
            OLD.id
            USING ERRCODE = '23514';
END IF;

RETURN OLD;
END;
$$;

CREATE TRIGGER trg_prevent_delete_currency_active
  BEFORE DELETE ON "Currencies"
  FOR EACH ROW EXECUTE FUNCTION prevent_delete_currency_active();

