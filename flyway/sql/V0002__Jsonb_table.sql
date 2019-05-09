CREATE TABLE jsonb_table (
    jsonb_doc jsonb
);

CREATE UNIQUE INDEX unique_index_jsonb_id ON jsonb_table using btree ((cast(jsonb_doc->>'id' as text)));

CREATE UNIQUE INDEX unique_index_jsonb_username on jsonb_table using btree ((cast(jsonb_doc->>'username' as text)));

CREATE INDEX index_jsonb_timestamp_field on jsonb_table using btree ((cast(jsonb_doc->>'timeStampInEpochMillis' as numeric)));

CREATE INDEX index_jsonb_date_field on jsonb_table using btree ((cast(jsonb_doc->>'dateInEpochDays' as numeric)));

CREATE INDEX index_jsonb_decimal_field on jsonb_table using btree ((cast(jsonb_doc->>'decimalField' as numeric)));

create index index_jsonb_all_tags on jsonb_table using gin ((jsonb_doc->'index_field_all_tags'));