CREATE TABLE simple_table (
    id uuid PRIMARY KEY,
    username text UNIQUE NOT NULL
);

INSERT INTO simple_table(id, username) VALUES ('9a7e2b9a-3340-4aa1-b2e7-c6fe85610b23', 'sample_user');

CREATE TABLE sample_table (
    id UUID PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    date_field timestamptz,
    long_field BIGINT,
    int_field INTEGER,
    double_field float8,
    boolean_field BOOLEAN,
    decimal_field numeric
);