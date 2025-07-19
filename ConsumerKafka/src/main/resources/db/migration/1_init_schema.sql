CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    description TEXT,
    rating DECIMAL(3, 2)
);

-- Column comments in English
COMMENT ON COLUMN products.id IS 'Unique identifier of the product (primary key)';
COMMENT ON COLUMN products.name IS 'Name of the product';
COMMENT ON COLUMN products.price IS 'Price of the product with two decimal places';
COMMENT ON COLUMN products.quantity IS 'Available stock quantity of the product';
COMMENT ON COLUMN products.description IS 'Detailed description of the product (optional)';
COMMENT ON COLUMN products.rating IS 'Average rating of the product, from 0.00 to 9.99 (optional)';
