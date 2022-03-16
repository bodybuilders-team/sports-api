INSERT INTO courses(name)
VALUES ('LEIC');

INSERT INTO students(course, number, name)
VALUES (1, 12345, 'Alice');

INSERT INTO students(course, number, name)
SELECT cid AS course, 12346 AS number, 'Bob' AS name
FROM courses
WHERE name = 'LEIC';
