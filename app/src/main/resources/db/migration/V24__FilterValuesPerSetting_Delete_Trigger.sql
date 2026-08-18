CREATE OR REPLACE FUNCTION prevent_delete_filter_value_per_setting_active()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM "RateBroadcastingSettings"
        WHERE id = OLD.setting_id
          AND broadcasting_status = 'ACTIVE'
    ) THEN
        RAISE EXCEPTION
            'Cannot delete FilterValuesPerSetting for active setting %',
            OLD.setting_id
            USING ERRCODE = '23514';
END IF;

RETURN OLD;
END;
$$;

CREATE TRIGGER trg_prevent_delete_filter_value_per_setting_active
  BEFORE DELETE ON "FilterValuesPerSetting"
  FOR EACH ROW EXECUTE FUNCTION prevent_delete_filter_value_per_setting_active();
