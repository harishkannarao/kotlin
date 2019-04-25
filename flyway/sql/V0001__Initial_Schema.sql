CREATE TABLE simple_table (
    id uuid PRIMARY KEY,
    username text UNIQUE NOT NULL
);

INSERT INTO simple_table(id, username) VALUES ('9a7e2b9a-3340-4aa1-b2e7-c6fe85610b23', 'sample_user')