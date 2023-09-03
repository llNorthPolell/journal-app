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
	
	
-- objective
CREATE OR REPLACE FUNCTION check_completed_objective() RETURNS trigger AS $$
	DECLARE 
		o_completion_criteria VARCHAR(10);
		completed BOOLEAN;
	BEGIN 
		SELECT completion_criteria INTO o_completion_criteria
		FROM objective
		WHERE id = NEW.objective;

		IF o_completion_criteria = 'AND' THEN
			SELECT CASE WHEN COUNT(*) > 0 THEN FALSE ELSE TRUE END INTO completed 
			FROM (
					SELECT 
						CASE WHEN a.current_value = a.target_value and a.compare_type='=' THEN
							'COMPLETE'
						WHEN a.current_value >= a.target_value and a.compare_type='>=' THEN
							'COMPLETE'
						WHEN a.current_value <= a.target_value and a.compare_type='<=' THEN
							'COMPLETE'
						WHEN a.current_value > a.target_value and a.compare_type='>' THEN
							'COMPLETE'
						WHEN a.current_value < a.target_value and a.compare_type='<' THEN
							'COMPLETE'
						WHEN a.current_value <> a.target_value and a.compare_type='<>' THEN
							'COMPLETE'
						ELSE 
							'NOT COMPLETE' 
						END as result
					FROM (
						SELECT 
							o.id as objective_id,
							p.id as progress_id,
							*
						FROM objective o
						LEFT JOIN progress p
						ON o.id=p.objective
						WHERE o.id=NEW.objective
					) a
			) b
			WHERE b.result = 'NOT COMPLETE';
		
		ELSIF o_completion_criteria = 'OR' THEN
			SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END INTO completed 
			FROM (
					SELECT 
						CASE WHEN a.current_value = a.target_value and a.compare_type='=' THEN
							'COMPLETE'
						WHEN a.current_value >= a.target_value and a.compare_type='>=' THEN
							'COMPLETE'
						WHEN a.current_value <= a.target_value and a.compare_type='<=' THEN
							'COMPLETE'
						WHEN a.current_value > a.target_value and a.compare_type='>' THEN
							'COMPLETE'
						WHEN a.current_value < a.target_value and a.compare_type='<' THEN
							'COMPLETE'
						WHEN a.current_value <> a.target_value and a.compare_type='<>' THEN
							'COMPLETE'
						ELSE 
							'NOT COMPLETE' 
						END as result
					FROM (
						SELECT 
							o.id as objective_id,
							p.id as progress_id,
							*
						FROM objective o
						LEFT JOIN progress p
						ON o.id=p.objective
						WHERE o.id=NEW.objective
					) a
			) b	
			WHERE b.result = 'COMPLETE';
		ELSE 
			completed := FALSE;
		END IF;
		
		IF completed THEN 
			UPDATE objective
			SET date_completed=NEW.last_checked_entry_date,
			status='COMPLETE'
			WHERE id=NEW.objective;
		END IF;

		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;	


CREATE OR REPLACE TRIGGER check_completed_objective AFTER UPDATE ON progress
    FOR EACH ROW EXECUTE FUNCTION check_completed_objective();	
	
	