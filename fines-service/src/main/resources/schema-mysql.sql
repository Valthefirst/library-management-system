USE `fines-db`;

create table if not exists fines (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fine_id VARCHAR(36),
    amount DECIMAL(19,2),
    reason VARCHAR(50),
    is_paid VARCHAR(36)
    );
