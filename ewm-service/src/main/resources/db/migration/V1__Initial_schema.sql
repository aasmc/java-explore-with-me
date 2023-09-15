CREATE TABLE IF NOT EXISTS categories(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(64) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    email VARCHAR(256) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN NOT NULL,
    title VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events(
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
    compilation_id BIGINT,
    CONSTRAINT EVENTS_USER_ID_FK FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
    CONSTRAINT EVENTS_CATEGORY_ID_FK FOREIGN KEY (category_id) REFERENCES CATEGORIES(id),
    CONSTRAINT EVENTS_COMPILATION_ID_FK FOREIGN KEY (compilation_id) REFERENCES COMPILATIONS(id)
);

CREATE INDEX IF NOT EXISTS EVENTS_USER_ID_FK_IDX ON events(user_id);
CREATE INDEX IF NOT EXISTS EVENTS_CATEGORY_ID_FK_IDX ON events(category_id);
CREATE INDEX IF NOT EXISTS EVENTS_EVENT_DATE_IDX ON events(event_date);


CREATE TABLE IF NOT EXISTS requests(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT NOT NULL,
    requester BIGINT NOT NULL,
    status VARCHAR(16) NOT NULL,
    CONSTRAINT REQUESTS_REQUESTER_FK FOREIGN KEY (requester) REFERENCES users(id),
    CONSTRAINT REQUESTS_EVENT_ID_FK FOREIGN KEY (event_id) REFERENCES events(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS REQUESTS_REQUESTER_EVENT_IDX ON requests(requester, event_id);

CREATE TABLE IF NOT EXISTS locations(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    radius FLOAT NOT NULL,
    name VARCHAR(50) NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS LOCATIONS_LAT_LON_UNQ_IDX ON locations(lat, lon);

CREATE OR REPLACE FUNCTION distance(lat1 float, lon1 float, lat2 float, lon2 float)
    RETURNS float
AS
'
    declare
        dist float = 0;
        rad_lat1 float;
        rad_lat2 float;
        theta float;
        rad_theta float;
    BEGIN
        IF lat1 = lat2 AND lon1 = lon2
        THEN
            RETURN dist;
        ELSE
            -- переводим градусы широты в радианы
            rad_lat1 = pi() * lat1 / 180;
            -- переводим градусы долготы в радианы
            rad_lat2 = pi() * lat2 / 180;
            -- находим разность долгот
            theta = lon1 - lon2;
            -- переводим градусы в радианы
            rad_theta = pi() * theta / 180;
            -- находим длину ортодромии
            dist = sin(rad_lat1) * sin(rad_lat2) + cos(rad_lat1) * cos(rad_lat2) * cos(rad_theta);

            IF dist > 1
            THEN dist = 1;
            END IF;

            dist = acos(dist);
            -- переводим радианы в градусы
            dist = dist * 180 / pi();
            -- переводим градусы в километры
            dist = dist * 60 * 1.8524;

            RETURN dist;
        END IF;
    END;
'
    LANGUAGE PLPGSQL;
