INSERT INTO trail (id, zone) VALUES (1, 'Zone 1');

INSERT INTO coordinate (x, y) VALUES (1.1, 2.2);
INSERT INTO coordinate (x, y) VALUES (3.3, 4.4);

INSERT INTO place (name, coordinate, type) VALUES ('Place 1', 1, 'Type 1');
INSERT INTO place (name, coordinate, type) VALUES ('Place 2', 2, 'Type 2');

INSERT INTO trail_path (trail_id, path) VALUES (1, 1);
INSERT INTO trail_path (trail_id, path) VALUES (1, 2);

INSERT INTO trail_places (trail_id, places) VALUES (1, 1);
INSERT INTO trail_places (trail_id, places) VALUES (1, 2);