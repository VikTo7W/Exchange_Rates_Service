CREATE OR REPLACE FUNCTION deactivate_settings_on_source_down()
  RETURNS TRIGGER
  LANGUAGE plpgsql
AS $$
BEGIN
  IF OLD.status IS DISTINCT FROM NEW.status
    AND NEW.status = 'DOWN' THEN

    UPDATE "RateBroadcastingSettings"
    SET broadcasting_status = 'INACTIVE'
    WHERE source_id = NEW.id
      AND broadcasting_status <> 'INACTIVE';

  END IF;

  RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trg_deactivate_settings_on_source_down
  ON "RateSources";

CREATE TRIGGER trg_deactivate_settings_on_source_down
  AFTER UPDATE OF status
  ON "RateSources"
  FOR EACH ROW
EXECUTE FUNCTION deactivate_settings_on_source_down();
