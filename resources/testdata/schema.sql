DROP TABLE IF EXISTS currency_conversions;

CREATE TABLE currency_conversions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    from_currency VARCHAR(3) NOT NULL,
    to_currency VARCHAR(3) NOT NULL,
    amount INT NOT NULL
);
