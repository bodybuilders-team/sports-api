INSERT INTO users(name, email)
VALUES ('André Jesus', 'A48280@alunos.isel.pt'),
       ('André Páscoa', 'A48089@alunos.isel.pt'),
       ('Nyckollas Brandão', 'A48287@alunos.isel.pt');

INSERT INTO tokens(token, uid)
VALUES ('49698b60-12ca-4df7-8950-d783124f5fas', 1),
       ('537b4555-161a-40fd-b9d6-290800b73dc8', 2),
       ('2ccc299a-db9a-414f-bcab-28ecb541146b', 3);

INSERT INTO sports(name, description, uid)
VALUES ('Soccer', 'Kick a ball to score a goal', 1),
       ('Powerlifting', 'Get big', 2),
       ('Basketball', 'Shoot a ball through a hoop', 3);

INSERT INTO routes(start_location, end_location, distance, uid)
VALUES ('Odivelas', 'Chelas', 150, 1),
       ('Chelas', 'Odivelas', 150, 2),
       ('Lisboa', 'Porto', 1500, 3);

INSERT INTO activities(date, duration, uid, sid, rid)
VALUES (date '2022-11-20', '72:44:63.903', 1, 2, 1);
