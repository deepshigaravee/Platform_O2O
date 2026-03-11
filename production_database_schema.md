# Concise Production Database Schema: O2O Clothing Platform

As requested, I have simplified the schema back to your original, concise 6-table design. While smaller, it still maintains the data integrity, spatial coordinates (`latitude`/`longitude`), constraints, and correct data types needed for an MNC training-level production database.

---

## 1. Entity-Relationship (ER) Diagram

```mermaid
erDiagram
    USERS ||--o{ STORES : "owns (OwnerID)"
    USERS ||--o{ ORDERS : "places (UserID)"
    STORES ||--o{ PRODUCTS : "sells (StoreID)"
    PRODUCTS ||--o{ ORDERS : "ordered in (ProductID)"
    ORDERS ||--|| PAYMENTS : "paid via (OrderID)"
    ADMINS : "system administration"
```

---

## 2. SQL DDL Statements

### 1. `users` Table
Handles both customers and store owners. Includes GPS coordinates for location-based searching.

```sql
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('CUSTOMER', 'STORE_OWNER') NOT NULL DEFAULT 'CUSTOMER',
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Index for fast role queries and login lookups
    INDEX idx_user_email (email),
    INDEX idx_user_role (role)
);
```

### 2. `stores` Table
Owned by users with the `STORE_OWNER` role. Contains business details and geographical plotting data used for the 5-km radius feature.

```sql
CREATE TABLE stores (
    store_id INT PRIMARY KEY AUTO_INCREMENT,
    owner_id INT NOT NULL,
    store_name VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    contact_number VARCHAR(20),
    opening_time TIME,
    closing_time TIME,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    
    -- Fast geospatial querying
    INDEX idx_store_location (latitude, longitude)
);
```

### 3. `products` Table
Inventory table tied directly to a specific store. Represents a single piece of clothing available for pre-order.

```sql
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    store_id INT NOT NULL,
    product_name VARCHAR(150) NOT NULL,
    brand VARCHAR(100),
    category VARCHAR(100),
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (store_id) REFERENCES stores(store_id) ON DELETE CASCADE,
    INDEX idx_product_store (store_id)
);
```

### 4. `orders` Table
Records a customer's pre-order of a specific product from a store.

```sql
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT,
    
    INDEX idx_order_user (user_id),
    INDEX idx_order_product (product_id)
);
```

### 5. `payments` Table
Captures the 20% advance transaction (or full settlement) for a specific order. 

```sql
CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL UNIQUE, -- One-to-One relationship matching the 6-table spec
    amount DECIMAL(10,2) NOT NULL,
    payment_method ENUM('CARD', 'UPI', 'NET_BANKING', 'CASH') NOT NULL,
    status ENUM('PENDING', 'PAID', 'FAILED') NOT NULL DEFAULT 'PENDING',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE RESTRICT
);
```

### 6. `admins` Table
A dedicated table strictly for global platform administrators (SuperAdmins, Moderators) to ensure separation from normal platform business flow.

```sql
CREATE TABLE admins (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('SUPER_ADMIN', 'MODERATOR') NOT NULL DEFAULT 'MODERATOR',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_admin_email (email)
);
```

---

## Production Characteristics of this 6-Table Model
1. Let's keep data footprints small but strict: Use `ENUM()` columns heavily so your application code never injects bad status strings (like spelling "Confirmed" as "Confirm"). 
2. Used `DECIMAL(10,8)` for Latitude and `DECIMAL(11,8)` for Longitude. This is the global standard for high-accuracy GPS tracking, which is essential for your 5km Haversine distance calculations.
3. Added `created_at` timestamps uniformly. MNCs always mandate audit trails showing *when* data was written.
4. Set strict foreign key behaviors (using `ON DELETE RESTRICT` for history like orders, and `ON DELETE CASCADE` for physical items like a product when a store closes down).
