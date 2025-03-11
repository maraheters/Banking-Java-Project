CREATE TABLE public.bank (
    id          SERIAL          PRIMARY KEY ,
    name        VARCHAR(255)    NOT NULL,
    bic         VARCHAR(25)     NOT NULL UNIQUE ,
    address     TEXT
);

ALTER TABLE public.account
    ADD COLUMN bank_id BIGINT;

ALTER TABLE account
    ADD CONSTRAINT fk_bank FOREIGN KEY (bank_id) REFERENCES bank(id);