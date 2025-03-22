CREATE TABLE public.deposit (
    id                  SERIAL              PRIMARY KEY ,
    minimum             NUMERIC(19, 4)      NOT NULL,
    bonus               NUMERIC(19, 4),
    status              VARCHAR(50)         NOT NULL,
    created_at          TIMESTAMP           NOT NULL,
    length_in_months    INTEGER             NOT NULL,
    account_id          INTEGER             NOT NULL,
    interest_rate       NUMERIC(19, 4)      NOT NULL,
    last_bonus_date     TIMESTAMP,
    number_of_bonuses   INTEGER,

    CONSTRAINT fk_account
        FOREIGN KEY (account_id) REFERENCES public.personal_account(id)
);
