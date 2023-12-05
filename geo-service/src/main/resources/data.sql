-- Create Trail if not exists
INSERT INTO trail (id, zone)
SELECT 1, 'mac'
WHERE NOT EXISTS(SELECT 1 FROM trail WHERE id = 1);

-- Create Places if not exists
INSERT INTO place (id, name, type, CoordinateX, CoordinateY, trail_id)
SELECT 1, 'shelter1', 'shelter', 14.0, 3.0, 1
WHERE NOT EXISTS(SELECT 1 FROM place WHERE id = 1);

INSERT INTO place (id, name, type, CoordinateX, CoordinateY, trail_id)
SELECT 2, 'shelter2', 'shelter', 18.0, 6.0, 1
WHERE NOT EXISTS(SELECT 1 FROM place WHERE id = 2);

INSERT INTO place (id, name, type, CoordinateX, CoordinateY, trail_id)
SELECT 3, 'shelter3', 'shelter', 14.0, 11.0, 1
WHERE NOT EXISTS(SELECT 1 FROM place WHERE id = 3);

INSERT INTO place (id, name, type, CoordinateX, CoordinateY, trail_id)
SELECT 4, 'shelter4', 'shelter', 45.0, 36.0, 1
WHERE NOT EXISTS(SELECT 1 FROM place WHERE id = 4);

INSERT INTO place (id, name, type, CoordinateX, CoordinateY, trail_id)
SELECT 5, 'shelter5', 'shelter', 7.0, 89.0, 1
WHERE NOT EXISTS(SELECT 1 FROM place WHERE id = 5);