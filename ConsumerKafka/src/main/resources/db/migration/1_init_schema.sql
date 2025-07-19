-- Crear la tabla 'productos'
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    description TEXT,
    rating DECIMAL(3, 2)
);
