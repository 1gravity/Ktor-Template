-- Customer
CREATE TYPE customer_status AS ENUM ('Active', 'Blocked', 'Deleted');
CREATE TYPE language_enum AS ENUM ('en', 'de');

CREATE TABLE customer(
    id SERIAL PRIMARY KEY,
    customer_uuid VARCHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL,
    first_name VARCHAR(127) NOT NULL,
    last_name VARCHAR(127) NOT NULL,
    status customer_status NOT NULL,
    language language_enum NOT NULL,
    account_id INTEGER REFERENCES Account NOT NULL
--    account_id INTEGER NOT NULL,
--    CONSTRAINT fk_customer_account
--        FOREIGN KEY (account_id)
--        REFERENCES account (id)
);

