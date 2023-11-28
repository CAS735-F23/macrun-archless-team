-- Insert into 'trail' table
INSERT INTO trail (id, zone) VALUES (1, 'mac');

-- Insert into 'coordinate' table for path
INSERT INTO coordinate (id, x, y, trail_id) VALUES
                                                (1, 0, 0, 1),
                                                (2, 14, 3, 1),
                                                (3, 18, 6, 1),
                                                (4, 14, 11, 1),
                                                (5, 45, 36, 1),
                                                (6, 7, 89, 1);

-- Insert into 'coordinate' table for places
INSERT INTO coordinate (id, x, y, trail_id) VALUES
                                                (7, 14, 3, 1),
                                                (8, 18, 6, 1),
                                                (9, 14, 11, 1),
                                                (10, 45, 36, 1),
                                                (11, 7, 89, 1);

-- Insert into 'place' table
INSERT INTO place (id, name, coordinate_id, type, trail_id) VALUES
                                                                (1, 'shelter1', 7, 'shelter', 1),
                                                                (2, 'shelter2', 8, 'shelter', 1),
                                                                (3, 'shelter3', 9, 'shelter', 1),
                                                                (4, 'shelter4', 10, 'shelter', 1),
                                                                (5, 'shelter5', 11, 'shelter', 1);