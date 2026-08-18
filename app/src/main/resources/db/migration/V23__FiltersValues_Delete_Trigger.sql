CREATE OR REPLACE FUNCTION prevent_delete_filter_value_active()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM "Filters" f
        JOIN "RateBroadcastingSettings" rbs
          ON rbs.source_id = f.source_id
        WHERE f.id = OLD.filter_id
          AND rbs.broadcasting_status = 'ACTIVE'
    ) THEN
        RAISE EXCEPTION
            'Cannot delete FilterValue %, source used by active settings',
            OLD.id
            USING ERRCODE = '23514';
END IF;
RETURN OLD;
END;
$$;

CREATE TRIGGER trg_prevent_delete_filter_value_active
  BEFORE DELETE ON "FiltersValues"
  FOR EACH ROW EXECUTE FUNCTION prevent_delete_filter_value_active();
