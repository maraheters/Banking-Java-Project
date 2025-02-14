CREATE TABLE public.account
(
    id              serial          PRIMARY KEY,
    iban            varchar(34)     NOT NULL,
    status          varchar(50)     NOT NULL,
    type            varchar(50)     NOT NULL,
    balance         numeric(19, 4)  DEFAULT 0.00 NOT NULL,
    date_created    timestamp       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    holder_id       bigint
);

ALTER TABLE public.deposit
    ADD CONSTRAINT fk_account
        FOREIGN KEY (account_id) REFERENCES public.account(id);