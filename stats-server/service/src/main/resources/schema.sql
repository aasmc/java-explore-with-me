CREATE TABLE IF NOT EXISTS HITS(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app VARCHAR(128) NOT NULL,
    uri VARCHAR(128) NOT NULL,
    ip VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS HITS_TIMESTAMP_IDX ON HITS(timestamp);