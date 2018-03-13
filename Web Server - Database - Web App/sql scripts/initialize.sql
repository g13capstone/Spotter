
-- Drop Tables
DROP TABLE ParkingStalls;
DROP TABLE Comments;
DROP TABLE ParkingLots;
DROP TABLE Locations;

--Create TABLES
CREATE TABLE Locations (
	location	VARCHAR(100)	NOT NULL,
	latitude 	FLOAT(10,6),
	longitude	FLOAT(10,6),
	PRIMARY KEY(location)
);

CREATE TABLE ParkingLots (
	lot_id		INT(5)			NOT NULL,
	lot_name	VARCHAR(20),
	location	VARCHAR(100),
	price		DECIMAL(5,2),
	latitude	FLOAT(10,6),
	longitude	FLOAT(10,6),
	temp		DECIMAL(4,2),
	image		varchar(30),
	PRIMARY KEY (lot_id),
	FOREIGN KEY (location) REFERENCES Locations(location)
);

CREATE TABLE ParkingStalls (
	lot_id 		INT(5)		NOT NULL,
	stall_id	INT(3),
	status		BOOLEAN,
	PRIMARY KEY(lot_id, stall_id),
	FOREIGN KEY (lot_id) REFERENCES ParkingLots(lot_id)
);

CREATE TABLE Comments (
	lot_id		INT(5),
	username	VARCHAR(20),
	logtime		DATETIME,
	text		VARCHAR(200),
	PRIMARY KEY (lot_id, username, logtime),
	FOREIGN KEY (lot_id) REFERENCES ParkingLots(lot_id)
);

--END

