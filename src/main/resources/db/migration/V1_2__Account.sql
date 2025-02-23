CREATE TABLE public.account (
    id              serial          PRIMARY KEY,
    iban            varchar(34)     NOT NULL,
    status          varchar(50)     NOT NULL,
    type            varchar(50)     NOT NULL,
    balance         numeric(19, 4)  NOT NULL,
    date_created    timestamp       NOT NULL,
    holder_id       bigint          NOT NULL
);

ALTER TABLE public.deposit
    ADD CONSTRAINT fk_account
        FOREIGN KEY (account_id) REFERENCES public.account(id);
