-- Account
CREATE TYPE account_status AS ENUM ('Active', 'Blocked', 'Deleted');

CREATE TABLE account(
    id SERIAL PRIMARY KEY,
    account_uuid VARCHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL,
    status account_status NOT NULL
);
