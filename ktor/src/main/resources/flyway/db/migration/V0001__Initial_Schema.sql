CREATE TABLE sample_table (
    id UUID PRIMARY KEY,
    username TEXT NOT NULL,
    date_field timestamptz,
    long_field BIGINT,
    int_field INTEGER,
    double_field float8,
    boolean_field BOOLEAN,
    decimal_field numeric
);

CREATE UNIQUE INDEX unique_index_username ON sample_table (username);

ALTER TABLE sample_table ADD CONSTRAINT unique_index_username_constraint UNIQUE USING INDEX unique_index_username;

CREATE INDEX index_date_field ON sample_table (date_field);