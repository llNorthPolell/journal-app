-- dashboard_widget
INSERT INTO dashboard_widget(journal, creation_timestamp, last_updated, type, position, title)
VALUES (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'last-entry', 0, 'Last Entry')

INSERT INTO dashboard_widget(journal, creation_timestamp, last_updated, type, position, title)
VALUES (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'line-graph', 1, 'Test')

SELECT *
FROM dashboard_widget



-- chart_data_config
INSERT INTO widget_data_config (widget,type,label,rule)
VALUES (2,'x','Date','dateOfEntry');

INSERT INTO widget_data_config (widget,type,label,rule, color)
VALUES (2,'y','Current','a', '#B72714');

INSERT INTO widget_data_config (widget,type,label,rule, color)
VALUES (2,'y','Target','targetA', '#1E54BA');

SELECT *
FROM widget_data_config


-- objective
INSERT INTO objective (goal,journal,icon,status,topic,description,completion_criteria,creation_timestamp,last_updated)
VALUES ('e6dc531c-2427-46bd-a7c6-748f86fd33ae',3,'someIcon.png','IN PROGRESS','test','Create one goal and test two objective tasks','AND', current_timestamp, current_timestamp);


-- progress
INSERT INTO progress (objective,rec_key,current_value,compare_type,target_value)
VALUES (1,'Goals Created',0,'>=',1); 

INSERT INTO progress (objective,rec_key,current_value,compare_type,target_value)
VALUES (1,'Tasks Tested',0,'=',2); 