SET TIME ZONE 'UTC';

ALTER TABLE users ADD COLUMN balance numeric(12, 2) NOT NULL DEFAULT 0.00;

CREATE TABLE expenses (
    id uuid NOT NULL PRIMARY KEY,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    user_id uuid NOT NULL REFERENCES users(id),
    amount numeric(12, 2) NOT NULL,
    kind varchar(20) NOT NULL,
    item_name varchar(255),
    bean_name varchar(255),
    weight_grams int,
    note varchar(500)
);

CREATE TABLE payments (
    id uuid NOT NULL PRIMARY KEY,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    user_id uuid NOT NULL REFERENCES users(id),
    amount numeric(12, 2) NOT NULL,
    note varchar(500)
);
