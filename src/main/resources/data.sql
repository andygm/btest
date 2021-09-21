DROP TABLE IF EXISTS phones;

CREATE TABLE phones (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    brand VARCHAR(250) NOT NULL,
    device VARCHAR(250) NOT NULL,
    technology VARCHAR(250) DEFAULT NULL,
    bands2g VARCHAR(250) DEFAULT NULL,
    bands3g VARCHAR(250) DEFAULT NULL,
    bands4g VARCHAR(250) DEFAULT NULL,
    available BOOLEAN DEFAULT TRUE,
    last_booked TIMESTAMP DEFAULT NULL,
    last_booked_person VARCHAR(250) DEFAULT NULL
);

INSERT INTO phones (brand, device) VALUES
    ('Samsung', 'Galaxy S9'),
    ('Samsung', 'Galaxy S8'),
    ('Samsung', 'Galaxy S7'),
    ('Motorola', 'Nexus 6'),
    ('LG', 'Nexus 5X'),
    ('Huawei', 'Honor 7X'),
    ('Apple', 'iPhone X'),
    ('Apple', 'iPhone 8'),
    ('Apple', 'iPhone 4s'),
    ('Nokia', '3310');
