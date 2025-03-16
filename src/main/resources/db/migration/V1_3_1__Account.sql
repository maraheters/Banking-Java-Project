CREATE TABLE public.account (
    id              SERIAL          PRIMARY KEY,
    iban            VARCHAR(34)     NOT NULL,
    status          VARCHAR(50)     NOT NULL,
    balance         NUMERIC(19, 4)  NOT NULL,
    created_at      TIMESTAMP       NOT NULL,
    bank_id         BIGINT,

    CONSTRAINT fk_bank
        FOREIGN KEY (bank_id) REFERENCES public.bank(id)
);
