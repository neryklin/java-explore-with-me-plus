CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS locations
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    annotation VARCHAR(2000),
    initiator_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(120),
    description VARCHAR(7000),
    category_id INTEGER REFERENCES categories(id) ON DELETE CASCADE,
    location_id INTEGER REFERENCES locations(id) ON DELETE CASCADE,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    paid BOOLEAN,
    request_moderation BOOLEAN,
    participant_limit BIGINT,
    confirmed_requests BIGINT,
    state VARCHAR(120),
    created_on TIMESTAMP WITHOUT TIME ZONE,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    views BIGINT
);

