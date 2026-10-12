CREATE TABLE test_categories (
    id uuid NOT NULL,
    name varchar(255),
    requires_fasting boolean NOT NULL,
    PRIMARY KEY (id)
);
