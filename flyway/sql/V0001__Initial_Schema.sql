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