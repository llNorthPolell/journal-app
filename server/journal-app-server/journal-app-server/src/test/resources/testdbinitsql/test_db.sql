-- TABLE CREATION
CREATE TABLE journal
(id SERIAL PRIMARY KEY NOT NULL,
name VARCHAR(255) NOT NULL,
author VARCHAR(255) NOT NULL,
img TEXT DEFAULT NULL,
creation_timestamp TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL,
last_updated TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL
);

CREATE TABLE dashboard_widget
(id SERIAL PRIMARY KEY NOT NULL,
journal INTEGER REFERENCES journal,
creation_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
type VARCHAR(255) NOT NULL,
position INTEGER NOT NULL,
title VARCHAR(255) DEFAULT NULL
);

CREATE TABLE widget_data_config
(id SERIAL PRIMARY KEY NOT NULL,
widget INTEGER REFERENCES dashboard_widget,
type VARCHAR(1) NOT NULL,
label VARCHAR(255) NOT NULL,
color VARCHAR(7) NOT NULL,
rule VARCHAR(255) NOT NULL
);

-- TRIGGER CREATION
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

--- dashboard_widget
INSERT INTO dashboard_widget(journal, creation_timestamp, last_updated, type, position, title)
VALUES (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'last-entry', 0, 'Last Entry');

INSERT INTO dashboard_widget(journal, creation_timestamp, last_updated, type, position, title)
VALUES (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'line-graph', 1, 'Test');

INSERT INTO dashboard_widget(journal, creation_timestamp, last_updated, type, position, title)
VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'line-graph', 1, 'Test Another Journal');


--- chart_data_config
INSERT INTO widget_data_config (widget,type,label,rule)
VALUES (2,'x','Date','dateOfEntry');

INSERT INTO widget_data_config (widget,type,label,rule,color)
VALUES (2,'y','Current','a','#B72714');

INSERT INTO widget_data_config (widget,type,label,rule,color)
VALUES (2,'y','Target','targetA','#1E54BA');

INSERT INTO widget_data_config (widget,type,label,rule,color)
VALUES (3,'y','My Values','b','#000000');