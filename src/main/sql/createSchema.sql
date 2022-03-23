DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS sports CASCADE;
DROP TABLE IF EXISTS routes CASCADE;
DROP TABLE IF EXISTS activities CASCADE;

CREATE TABLE users
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(60)  NOT NULL,
    email VARCHAR(320) NOT NULL
);

CREATE TABLE sports
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(30) NOT NULL,
    description VARCHAR(1000),
    user_id     INT         NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE routes
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(30)  NOT NULL,
    distance       INT          NOT NULL,
    start_location VARCHAR(250) NOT NULL,
    end_location   VARCHAR(250) NOT NULL,
    user_id        INT          NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE activities
(
    id       SERIAL PRIMARY KEY,
    date     DATE             NOT NULL,
    duration DOUBLE PRECISION NOT NULL,
    user_id  INT              NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    sport_id INT              NOT NULL REFERENCES sports (id) ON DELETE CASCADE ON UPDATE CASCADE,
    route_id INT REFERENCES routes (id) ON DELETE CASCADE ON UPDATE CASCADE
);
