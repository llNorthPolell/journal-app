-- TABLE CREATION
-- journal
CREATE TABLE journal
(id SERIAL PRIMARY KEY NOT NULL,
name VARCHAR(255) NOT NULL,
author VARCHAR(255) NOT NULL,
img TEXT DEFAULT NULL,
creation_timestamp TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL,
last_updated TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL
);

-- objective
CREATE TABLE objective
(id SERIAL PRIMARY KEY NOT NULL,
goal UUID NOT NULL,
journal INTEGER REFERENCES journal NOT NULL,
icon VARCHAR(255),
status VARCHAR(16),
topic VARcHAR(255),
description VARCHAR(255) NOT NULL,
completion_criteria VARCHAR(255),
creation_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
date_completed TIMESTAMP WITHOUT TIME ZONE,
deadline TIMESTAMP WITHOUT TIME ZONE
);


-- progress
CREATE TABLE progress
(id SERIAL PRIMARY KEY NOT NULL,
objective INTEGER REFERENCES objective NOT NULL,
rec_key VARCHAR(255) NOT NULL,
current_value DOUBLE PRECISION DEFAULT 0,
compare_type VARCHAR(3) NOT NULL,
target_value DOUBLE PRECISION NOT NULL,
last_checked_entry_date TIMESTAMP WITHOUT TIME ZONE
);


-- TRIGGER CREATION
-- objective
CREATE OR REPLACE FUNCTION check_completed_objective() RETURNS trigger AS
'
	DECLARE
		o_completion_criteria VARCHAR(10);
		completed BOOLEAN;
	BEGIN
		SELECT completion_criteria INTO o_completion_criteria
		FROM objective
		WHERE id = NEW.objective;

		IF o_completion_criteria = ''AND'' THEN
			SELECT CASE WHEN COUNT(*) > 0 THEN FALSE ELSE TRUE END INTO completed
			FROM (
					SELECT
						CASE WHEN a.current_value = a.target_value and a.compare_type=''='' THEN
							''COMPLETE''
						WHEN a.current_value >= a.target_value and a.compare_type=''>='' THEN
							''COMPLETE''
						WHEN a.current_value <= a.target_value and a.compare_type=''<='' THEN
							''COMPLETE''
						WHEN a.current_value > a.target_value and a.compare_type=''>'' THEN
							''COMPLETE''
						WHEN a.current_value < a.target_value and a.compare_type=''<'' THEN
							''COMPLETE''
						WHEN a.current_value <> a.target_value and a.compare_type=''<>'' THEN
							''COMPLETE''
						ELSE
							''NOT COMPLETE''
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
			WHERE b.result = ''NOT COMPLETE'';

		ELSIF o_completion_criteria = ''OR'' THEN
			SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END INTO completed
			FROM (
					SELECT
						CASE WHEN a.current_value = a.target_value and a.compare_type=''='' THEN
							''COMPLETE''
						WHEN a.current_value >= a.target_value and a.compare_type=''>='' THEN
							''COMPLETE''
						WHEN a.current_value <= a.target_value and a.compare_type=''<='' THEN
							''COMPLETE''
						WHEN a.current_value > a.target_value and a.compare_type=''>'' THEN
							''COMPLETE''
						WHEN a.current_value < a.target_value and a.compare_type=''<'' THEN
							''COMPLETE''
						WHEN a.current_value <> a.target_value and a.compare_type=''<>'' THEN
							''COMPLETE''
						ELSE
							''NOT COMPLETE''
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
			WHERE b.result = ''COMPLETE'';
		ELSE
			completed := FALSE;
		END IF;

		IF completed THEN
			UPDATE objective
			SET date_completed=NEW.last_checked_entry_date,
			status=''COMPLETE''
			WHERE id=NEW.objective;
		END IF;

		RETURN NEW;
	END;
' LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER check_completed_objective AFTER UPDATE ON progress
    FOR EACH ROW EXECUTE FUNCTION check_completed_objective();


-- INSERTS
--- journals
INSERT INTO journal (name,author,img,creation_timestamp,last_updated)
VALUES (
	'test',
	'northpole',
	'https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/c1a2ba41-1911-48a3-b9d8-9e21e84eaf88.PNG?alt=media&token=f845a0be-6d50-4afc-aef1-be0d6b6ee011',
	CURRENT_TIMESTAMP,
	CURRENT_TIMESTAMP
);

INSERT INTO journal (name,author,img,creation_timestamp,last_updated)
VALUES (
	'test2',
	'northpole',
	'https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/c1a2ba41-1911-48a3-b9d8-9e21e84eaf88.PNG?alt=media&token=f845a0be-6d50-4afc-aef1-be0d6b6ee011',
	CURRENT_TIMESTAMP,
	CURRENT_TIMESTAMP
);

INSERT INTO journal (name,author,img,creation_timestamp,last_updated)
VALUES (
	'test3',
	'northpole',
	'https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/c1a2ba41-1911-48a3-b9d8-9e21e84eaf88.PNG?alt=media&token=f845a0be-6d50-4afc-aef1-be0d6b6ee011',
	CURRENT_TIMESTAMP,
	CURRENT_TIMESTAMP
);

-- objective
INSERT INTO objective (id,goal,journal,icon,status,topic,description,completion_criteria,creation_timestamp,last_updated)
VALUES (7,'e6dc531c-2427-46bd-a7c6-748f86fd33ae',3,'someIcon.png','IN PROGRESS','test','Create one goal and test two objective tasks','AND',current_timestamp, current_timestamp);

INSERT INTO objective (id,goal,journal,icon,status,topic,description,completion_criteria,creation_timestamp,last_updated)
VALUES (8,'c74df696-0d90-425f-b989-c6d3a8351467',3,'someIcon.png','IN PROGRESS','test2','Create one goal or test two objective tasks','OR',current_timestamp, current_timestamp);

INSERT INTO objective (id,goal,journal,icon,status,topic,description,completion_criteria,creation_timestamp,last_updated)
VALUES (9,'6ffb1ae2-7adb-4cbd-a2b5-a2423d655e69',3,'someIcon.png','IN PROGRESS','test3','Create one goal and test two objective tasks (but only one should actually be tested in this test case)','AND',current_timestamp, current_timestamp);



-- progress
INSERT INTO progress (id, objective,rec_key,current_value,compare_type,target_value)
VALUES (4,7,'Goals Created',0,'>=',1);

INSERT INTO progress (id,objective,rec_key,current_value,compare_type,target_value)
VALUES (5,7,'Tasks Tested',0,'=',2);

INSERT INTO progress (id,objective,rec_key,current_value,compare_type,target_value)
VALUES (6,8,'Goals Created',0,'>=',1);

INSERT INTO progress (id,objective,rec_key,current_value,compare_type,target_value)
VALUES (7,8,'Tasks Tested',0,'=',2);

INSERT INTO progress (id,objective,rec_key,current_value,compare_type,target_value)
VALUES (8,9,'Goals Created',0,'>=',1);

INSERT INTO progress (id,objective,rec_key,current_value,compare_type,target_value)
VALUES (9,9,'Tasks Tested',0,'=',2);