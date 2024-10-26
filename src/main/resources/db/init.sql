CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(100),
    department VARCHAR(50)
);

INSERT INTO employees (first_name, last_name, department) VALUES ('Dwight', 'Schrute', 'Sales');
INSERT INTO employees (first_name, last_name, department) VALUES ('Jim', 'Halpert', 'Sales');
