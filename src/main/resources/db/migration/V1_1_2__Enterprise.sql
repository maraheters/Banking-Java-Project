CREATE TABLE public.enterprise (
    id              SERIAL          PRIMARY KEY,
    bank_id         BIGINT          NOT NULL,
    type            VARCHAR(10)     NOT NULL,
    legal_name      VARCHAR(255)    NOT NULL,
    unp             VARCHAR(20)     UNIQUE NOT NULL,
    legal_address   TEXT            NOT NULL,

    CONSTRAINT fk_bank
        FOREIGN KEY (bank_id) REFERENCES public.bank(id)
)