CREATE TABLE public.pending_client (
    id                      SERIAL          PRIMARY KEY,
    name                    VARCHAR(255)    NOT NULL,
    email                   VARCHAR(255)    NOT NULL UNIQUE,
    password_hash           VARCHAR(255)    NOT NULL,
    phone_number            VARCHAR(255)    NOT NULL UNIQUE,
    passport_number         VARCHAR(255)    NOT NULL UNIQUE,
    identification_number   VARCHAR(255)    NOT NULL UNIQUE,
    requested_at            TIMESTAMP       NOT NULL DEFAULT now(),
    status                  VARCHAR(16)     NOT NULL
);