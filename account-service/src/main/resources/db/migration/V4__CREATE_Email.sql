-- Email
CREATE TYPE email_status AS ENUM ('Confirmed', 'Unconfirmed');

CREATE TABLE email(
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL,
    email VARCHAR(320) NOT NULL,
    status email_status NOT NULL,
    customer_id INTEGER REFERENCES Customer NOT NULL
--    customer_id INTEGER NOT NULL,
--    CONSTRAINT fk_email_customer
--        FOREIGN KEY (customer_id)
--        REFERENCES customer (id)
);
