-- dashboard_widget
CREATE OR REPLACE FUNCTION move_to_last_position() RETURNS trigger AS $$
	DECLARE
		current_max INTEGER;
		
	BEGIN
		SELECT MAX(position) INTO current_max
		FROM dashboard_widget
		WHERE journal=NEW.journal;
		
		IF current_max IS NULL THEN
			NEW.position = 0;
		ELSE 
			NEW.position = current_max + 1;
		END IF;
		
		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;	

CREATE OR REPLACE TRIGGER move_to_last_position BEFORE INSERT ON dashboard_widget
    FOR EACH ROW EXECUTE FUNCTION move_to_last_position();