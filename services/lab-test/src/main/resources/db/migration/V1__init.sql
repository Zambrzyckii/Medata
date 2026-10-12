CREATE TABLE test_categories (
    id uuid NOT NULL,
    name varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE lab_tests (
    id uuid NOT NULL,
    name varchar(255),
    unit varchar(255),
    reference_min double precision NOT NULL,
    reference_max double precision NOT NULL,
    price numeric(38,2),
    category_id uuid NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_lab_tests_category FOREIGN KEY (category_id) REFERENCES test_categories (id)
);
