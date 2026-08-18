CREATE OR REPLACE FUNCTION trg_validate_orders_window()
RETURNS TRIGGER AS $$
DECLARE
v_rate_array_size INTEGER;
BEGIN

SELECT rs."rate_array_size"
INTO v_rate_array_size
FROM "RateSources" rs
WHERE rs."id" = NEW."source_id";

    IF NEW."orders_window_start" > v_rate_array_size THEN
        RAISE EXCEPTION
            'orders_window_start (%) must be <= rate_array_size (%) from RateSources',
            NEW."orders_window_start",
            v_rate_array_size
            USING ERRCODE = '23514';
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS validate_orders_window ON "RateBroadcastingSettings";

CREATE TRIGGER validate_orders_window
  BEFORE INSERT OR UPDATE
  ON "RateBroadcastingSettings"
  FOR EACH ROW
EXECUTE FUNCTION trg_validate_orders_window();
