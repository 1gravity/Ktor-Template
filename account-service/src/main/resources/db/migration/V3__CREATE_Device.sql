-- Device
CREATE TYPE device_status AS ENUM ('Untrusted', 'Trusted', 'Blocked');

CREATE TABLE device(
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL,
    device_id VARCHAR(63) NOT NULL,
    name VARCHAR(63),
    status device_status NOT NULL,
    customer_id INTEGER REFERENCES Customer NOT NULL
--    customer_id INTEGER NOT NULL,
--    CONSTRAINT fk_device_customer
--        FOREIGN KEY (customer_id)
--        REFERENCES customer (id)
);
