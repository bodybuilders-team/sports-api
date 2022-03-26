DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS tokens CASCADE;
DROP TABLE IF EXISTS sports CASCADE;
DROP TABLE IF EXISTS routes CASCADE;
DROP TABLE IF EXISTS activities CASCADE;

CREATE TABLE users
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(60)  NOT NULL,
    email VARCHAR(320) NOT NULL CHECK ( email ~ '^[A-Za-z0-9+_.-]+@(.+)$' )
);

CREATE TABLE tokens
(
    token CHAR(36) PRIMARY KEY,
    uid   INT NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE sports
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(30) NOT NULL,
    description VARCHAR(1000),
    uid         INT         NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE routes
(
    id             SERIAL PRIMARY KEY,
    start_location VARCHAR(250) NOT NULL,
    end_location   VARCHAR(250) NOT NULL,
    distance       INT          NOT NULL CHECK ( distance > 0 ),
    uid            INT          NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE activities
(
    id       SERIAL PRIMARY KEY,
    date     DATE     NOT NULL,
    duration CHAR(12) NOT NULL CHECK ( duration ~ '^[0-9]{2}:[0-9]{2}:[0-9]{2}\.[0-9]{3}$'),
    uid      INT      NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    sid      INT      NOT NULL REFERENCES sports (id) ON DELETE CASCADE ON UPDATE CASCADE,
    rid      INT REFERENCES routes (id) ON DELETE CASCADE ON UPDATE CASCADE
);
