CREATE OR REPLACE FUNCTION prevent_delete_calc_rule_active()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM "RateBroadcastingSettings"
        WHERE calculation_rule_id = OLD.id
          AND broadcasting_status = 'ACTIVE'
    ) THEN
        RAISE EXCEPTION
            'Cannot delete SettingCalculationRule %, used by active settings',
            OLD.id
            USING ERRCODE = '23514';
END IF;
RETURN OLD;
END;
$$;

CREATE TRIGGER trg_prevent_delete_calc_rule_active
  BEFORE DELETE ON "SettingCalculationRules"
  FOR EACH ROW EXECUTE FUNCTION prevent_delete_calc_rule_active();
