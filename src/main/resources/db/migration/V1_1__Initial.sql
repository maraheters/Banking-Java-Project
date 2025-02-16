CREATE TABLE public.deposit
(
    id                  serial              primary key,
    minimum             numeric(19, 4)      not null,
    bonus               numeric(19, 4),
    status              varchar(50)         not null,
    date_created        timestamp           not null,
    length_in_months    int                 not null,
    account_id          bigint              not null,
    interest_rate       double precision    not null,
    last_bonus_date     timestamp,
    number_of_bonuses   int
);

CREATE TABLE public.account
(
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

CREATE TABLE public.banking_user (
         id                     SERIAL          PRIMARY KEY,
         name                   VARCHAR(100)    NOT NULL,
         phone_number           VARCHAR(15)     NOT NULL UNIQUE,
         passport_number        VARCHAR(20)     NOT NULL UNIQUE,
         identification_number  VARCHAR(20)     NOT NULL UNIQUE,
         email                  VARCHAR(100)    NOT NULL UNIQUE
);

ALTER TABLE public.account
    ADD CONSTRAINT fk_banking_user
        FOREIGN KEY (holder_id) REFERENCES public.banking_user(id);
