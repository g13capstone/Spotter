INSERT INTO Locations VALUES ('University of Manitoba - Fort Garry Campus', 49.809444, -97.133018), 
('University of Manitoba - Bannatyne Campus', 49.904747, -97.161842), 
('Polo Park', 49.883781, -97.198975);

INSERT INTO ParkingLots VALUES (20401, 'Education Lot', 'University of Manitoba - Fort Garry Campus', 0.00, 49.809158, -97.137619, 21.63, 'Education Lot.jpg'),
(20402, 'Engineering Lot', 'University of Manitoba - Fort Garry Campus', 3.00, 49.807961, -97.133469, -10.83, 'Engineering Lot.jpg'),
(20403, 'Dentistry Lot', 'University of Manitoba - Bannatyne Campus', 1.50, 49.904545, -97.162903, -15.00, NULL),
(20498, 'Test Lot Brodie', 'University of Manitoba - Bannatyne Campus', 0.00, 49.903793, -97.160355, 0.00, NULL),
(20499, 'Test Lot Atrium', 'University of Manitoba - Fort Garry Campus', 0.00, 49.808182, -97.133347, 0.00, NULL);

INSERT INTO ParkingStalls VALUES (20401, 1, 1),
(20401, 2, 0),
(20401, 3, 1),
(20402, 1, 1),
(20402, 2, 0),
(20402, 3, 0),
(20402, 4, 1),
(20403, 1, 0),
(20403, 2, 0),
(20403, 3, 1),
(20498, 1, 1),
(20498, 2, 1),
(20498, 3, 1),
(20499, 1, 0),
(20499, 2, 1),
(20499, 3, 0);
