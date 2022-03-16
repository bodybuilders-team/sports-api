DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS courses;

CREATE TABLE courses
(
    cid  SERIAL PRIMARY KEY,
    name varchar(80)
);

CREATE TABLE students
(
    number int PRIMARY KEY,
    name   varchar(80),
    course int REFERENCES courses (cid)
);
