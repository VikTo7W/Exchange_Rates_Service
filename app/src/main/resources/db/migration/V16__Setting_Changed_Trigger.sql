CREATE OR REPLACE FUNCTION enforce_source_up_for_broadcasting_settings()
  RETURNS TRIGGER
  LANGUAGE plpgsql
AS $$
DECLARE
  source_status SOURCE_STATUS;
BEGIN
  IF TG_OP = 'INSERT'
    OR (TG_OP = 'UPDATE' AND OLD.source_id IS DISTINCT FROM NEW.source_id)
    OR (
       TG_OP = 'UPDATE'
         AND OLD.broadcasting_status = 'INACTIVE'
         AND NEW.broadcasting_status = 'ACTIVE'
       )
  THEN
    SELECT status
    INTO source_status
    FROM "RateSources"
    WHERE id = NEW.source_id;

    IF source_status IS DISTINCT FROM 'UP' THEN
      RAISE EXCEPTION
        'Cannot activate or reference RateSource id %: source status is %, expected UP',
        NEW.source_id,
        COALESCE(source_status::text, 'NULL')
        USING ERRCODE = '23503';
    END IF;
  END IF;

  RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trg_enforce_source_up
  ON "RateBroadcastingSettings";

CREATE TRIGGER trg_enforce_source_up
  BEFORE INSERT OR UPDATE OF source_id, broadcasting_status
  ON "RateBroadcastingSettings"
  FOR EACH ROW
EXECUTE FUNCTION enforce_source_up_for_broadcasting_settings();
