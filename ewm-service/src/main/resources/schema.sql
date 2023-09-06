CREATE TABLE IF NOT EXISTS CATEGORIES(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(64) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS USERS(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    email VARCHAR(256) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS EVENTS(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation VARCHAR(2024) NOT NULL,
    category_id BIGINT NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description TEXT NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id BIGINT NOT NULL,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    paid BOOLEAN NOT NULL,
    participation_limit INTEGER NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN NOT NULL,
    state VARCHAR(16) NOT NULL,
    title VARCHAR(128) NOT NULL,
    CONSTRAINT EVENTS_USER_ID_FK FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
    CONSTRAINT EVENTS_CATEGORY_ID_FK FOREIGN KEY (category_id) REFERENCES CATEGORIES(id)
);

CREATE INDEX IF NOT EXISTS EVENTS_USER_ID_FK_IDX ON EVENTS(user_id);
CREATE INDEX IF NOT EXISTS EVENTS_CATEGORY_ID_FK_IDX ON EVENTS(category_id);
CREATE INDEX IF NOT EXISTS EVENTS_EVENT_DATE_IDX ON EVENTS(event_date);

CREATE TABLE IF NOT EXISTS COMPILATIONS(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT NOT NULL,
    pinned BOOLEAN NOT NULL,
    title VARCHAR(64) NOT NULL,
    CONSTRAINT COMPILATIONS_EVENT_ID_FK FOREIGN KEY (event_id) REFERENCES EVENTS(id)
);

CREATE INDEX IF NOT EXISTS COMPILATIONS_EVENT_ID_FK_IDX ON COMPILATIONS(event_id);

CREATE TABLE IF NOT EXISTS REQUESTS(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT NOT NULL,
    requester BIGINT NOT NULL,
    CONSTRAINT REQUESTS_REQUESTER_FK FOREIGN KEY (requester) REFERENCES USERS(id),
    CONSTRAINT REQUESTS_EVENT_ID_FK FOREIGN KEY (event_id) REFERENCES EVENTS(id)
);

CREATE INDEX IF NOT EXISTS REQUESTS_REQUESTER_FK_IDX ON REQUESTS(requester);
CREATE INDEX IF NOT EXISTS REQUESTS_EVENT_ID_FK_IDX ON REQUESTS(event_id);