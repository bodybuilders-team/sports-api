INSERT INTO users(name, email)
VALUES ('André Jesus', 'A48280@alunos.isel.pt'),
       ('André Páscoa', 'A48089@alunos.isel.pt'),
       ('Nyckollas Brandão', 'A48287@alunos.isel.pt');

INSERT INTO sports(name, description, user_id)
VALUES ('Soccer', 'Kick a ball to score a goal', 1),
       ('Powerlifting', 'Get big', 2),
       ('Basketball', 'Shoot a ball through a hoop', 2);

INSERT INTO activities(date, duration, user_id, sport_id)
VALUES (date '2022-11-20', 10, 1, 2);
