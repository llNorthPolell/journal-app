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