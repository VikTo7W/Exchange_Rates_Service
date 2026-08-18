CREATE OR REPLACE FUNCTION prevent_delete_pair_per_source_active()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM "RateBroadcastingSettings"
        WHERE source_id = OLD.source_id
          AND broadcasting_status = 'ACTIVE'
    ) THEN
        RAISE EXCEPTION
            'Cannot delete CurrencyPairsPerSource (%, %), source used by active settings',
            OLD.pair_id, OLD.source_id
            USING ERRCODE = '23514';
END IF;

RETURN OLD;
END;
$$;

CREATE TRIGGER trg_prevent_delete_pair_per_source_active
  BEFORE DELETE ON "CurrencyPairsPerSource"
  FOR EACH ROW EXECUTE FUNCTION prevent_delete_pair_per_source_active();
