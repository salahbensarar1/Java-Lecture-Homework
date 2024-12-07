-- Table structure for table `entries`
CREATE TABLE IF NOT EXISTS entries (
                                       spectatorid INTEGER NOT NULL,
                                       matchid INTEGER NOT NULL,
                                       timestamp TEXT DEFAULT NULL,
                                       PRIMARY KEY (spectatorid, matchid),
                                       FOREIGN KEY (spectatorid) REFERENCES spectators(id),
                                       FOREIGN KEY (matchid) REFERENCES matches(id)
);

-- Dumping data for table `entries`
INSERT INTO entries (spectatorid, matchid, timestamp) VALUES
                                                          (1, 1, '10:10:10'),
                                                          (1, 15, '14:18:00'),
                                                          (2, 16, '15:44:00'),
                                                          (3, 5, '13:17:00'),
                                                          (4, 3, '15:36:00'),
                                                          (5, 19, '14:38:00'),
                                                          (6, 11, '16:08:00'),
                                                          (7, 7, '13:00:00'),
                                                          (8, 11, '16:42:00'),
                                                          (9, 19, '15:16:00'),
                                                          (10, 7, '13:26:00'),
                                                          (11, 11, '11:11:00'),
                                                          (11, 13, '13:07:00'),
                                                          (12, 8, '15:05:00'),
                                                          (13, 12, '13:58:00'),
                                                          (14, 18, '17:10:00'),
                                                          (15, 12, '13:05:00'),
                                                          (16, 1, '14:23:00'),
                                                          (17, 6, '16:20:00'),
                                                          (18, 8, '15:00:00'),
                                                          (21, 1, '12:12:12');

-- Table structure for table `matches`
CREATE TABLE IF NOT EXISTS matches (
                                       id INTEGER NOT NULL PRIMARY KEY,
                                       mdate TEXT DEFAULT NULL,
                                       startsat TEXT DEFAULT NULL,
                                       ticketprice REAL DEFAULT NULL,
                                       mtype TEXT DEFAULT NULL
);

-- Dumping data for table `matches`
INSERT INTO matches (id, mdate, startsat, ticketprice, mtype) VALUES
                                                                  (1, '2024-03-22', '15:00:00', 500.00, 'championship'),
                                                                  (2, '2019-04-20', '15:30:00', 1000.00, 'championship'),
                                                                  (3, '2000-05-11', '10:00:00', 1000.00, 'championship'),
                                                                  (4, '2018-11-03', '13:30:00', 500.00, 'championship'),
                                                                  (5, '2018-12-01', '13:30:00', 500.00, 'championship'),
                                                                  (6, '2018-08-15', '17:00:00', 200.00, 'cup'),
                                                                  (7, '2018-11-17', '13:30:00', 1000.00, 'championship'),
                                                                  (8, '2019-05-18', '16:00:00', 1111.00, 'cup'),
                                                                  (9, '2018-08-22', '17:00:00', 200.00, 'cup'),
                                                                  (10, '2018-09-22', '15:30:00', 500.00, 'championship'),
                                                                  (11, '2018-08-25', '17:00:00', 500.00, 'championship'),
                                                                  (12, '2018-10-06', '14:00:00', 1000.00, 'championship'),
                                                                  (13, '2018-10-20', '13:30:00', 500.00, 'championship'),
                                                                  (14, '2019-03-09', '13:30:00', 500.00, 'championship'),
                                                                  (15, '2018-09-26', '15:00:00', 500.00, 'cup'),
                                                                  (16, '2019-05-04', '16:00:00', 500.00, 'championship'),
                                                                  (17, '2018-10-24', '14:00:00', 1000.00, 'cup'),
                                                                  (18, '2018-08-11', '17:00:00', 500.00, 'championship'),
                                                                  (19, '2019-04-06', '15:30:00', 500.00, 'championship'),
                                                                  (20, '2010-01-01', '11:11:11', 1111.00, 'cup');

-- Table structure for table `spectators`
CREATE TABLE IF NOT EXISTS spectators (
                                          id INTEGER NOT NULL PRIMARY KEY,
                                          sname TEXT DEFAULT NULL,
                                          male INTEGER DEFAULT NULL,
                                          haspass INTEGER DEFAULT NULL
);

-- Dumping data for table `spectators`
INSERT INTO spectators (id, sname, male, haspass) VALUES
                                                      (1, 'salah ben sarar', 1, 0),
                                                      (2, 'Kertész Bence', -1, 0),
                                                      (3, 'Molnár Alex', -1, -1),
                                                      (4, 'Bóka Nátán', -1, -1),
                                                      (5, 'Réti Dániel', -1, -1),
                                                      (6, 'Varga Zsolt', -1, 0),
                                                      (7, 'Elek Miklós', -1, 0),
                                                      (8, 'Balázs Domonkos', -1, 0),
                                                      (9, 'Tóth Ákos', -1, -1),
                                                      (10, 'Sári Péter', -1, 0),
                                                      (11, 'Kiss Dorina', 0, -1),
                                                      (12, 'Csuka Beáta', 0, 0),
                                                      (13, 'Kun Tamás', -1, -1),
                                                      (14, 'Zsolczai Balázs', -1, -1),
                                                      (15, 'Bertók Dániel', -1, -1),
                                                      (16, 'Fehér Orsolya', 0, -1),
                                                      (17, 'Márton Hunor', -1, 0),
                                                      (18, 'Vass András', -1, 0),
                                                      (19, 'Szepes Edit', 0, 0),
                                                      (20, 'Kónya Barbara', 0, 0),
                                                      (21, 'Tóth Luca', 0, 0),
                                                      (23, 'Oláh Soma', -1, 0),
                                                      (24, 'Bajnai Krisztián', -1, 0),
                                                      (25, 'Csáki Márton', -1, 0),
                                                      (26, 'Deák István', -1, 0),
                                                      (27, 'Török Róbert', -1, 0),
                                                      (28, 'Bogdándi Dávid', -1, -1),
                                                      (29, 'Szolga Ádám', -1, 0),
                                                      (30, 'Molnár Márton', -1, -1),
                                                      (31, 'Árva Ákos', -1, 0),
                                                      (32, 'Schiss Bence', -1, -1),
                                                      (33, 'Szűcs Kornél', -1, 0),
                                                      (34, 'Stephe', 1, 1);

-- Enabling foreign keys for SQLite
PRAGMA foreign_keys = ON;
