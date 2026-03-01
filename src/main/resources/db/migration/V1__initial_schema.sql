-- Agency (multi-tenant root)
CREATE TABLE agency (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255)
);

-- User (for future JWT auth)
CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL,
    agency_id BIGINT REFERENCES agency(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_app_user_agency_id ON app_user(agency_id);

-- Ticket
CREATE TABLE ticket (
    id BIGSERIAL PRIMARY KEY,
    agency_id BIGINT NOT NULL REFERENCES agency(id),
    flat_number VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    summary VARCHAR(1000) NOT NULL,
    status VARCHAR(50) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE INDEX idx_ticket_status ON ticket(status);
CREATE INDEX idx_ticket_category ON ticket(category);
CREATE INDEX idx_ticket_created_at ON ticket(created_at);
CREATE INDEX idx_ticket_agency_id ON ticket(agency_id);
