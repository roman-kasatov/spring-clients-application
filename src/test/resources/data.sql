INSERT INTO client(name) VALUES ('Artur');
INSERT INTO client(name) VALUES ('Ivan');
INSERT INTO email(client_id, "value") VALUES
    (1, 'email1@test.com'),
    (1, 'email2@test.com');
INSERT INTO phone_number(client_id, "value") VALUES
    (1, '+7(999)111-11-11');