CREATE TABLE public.transaction (
    id                  SERIAL          PRIMARY KEY,
    from_entity_id      BIGINT,
    from_type           VARCHAR(50),    CHECK ( from_type IS NULL OR from_type IN ('ACCOUNT', 'DEPOSIT', 'LOAN', 'EXTERNAL') ),
    to_entity_id        BIGINT          NOT NULL,
    to_type             VARCHAR(50)     NOT NULL CHECK (to_type IN ('ACCOUNT', 'DEPOSIT', 'LOAN', 'EXTERNAL')),
    amount              NUMERIC(19,4)   NOT NULL CHECK (amount > 0),
    timestamp           TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transaction_from_entity ON transaction (from_entity_id, from_type);
CREATE INDEX idx_transaction_to_entity ON transaction (to_entity_id, to_type);