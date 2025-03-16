CREATE TABLE public.deposit (
    id                  serial              primary key,
    minimum             numeric(19, 4)      not null,
    bonus               numeric(19, 4),
    status              varchar(50)         not null,
    created_at          timestamp           not null,
    length_in_months    int                 not null,
    account_id          bigint              not null,
    interest_rate       numeric(19, 4)      not null,
    last_bonus_date     timestamp,
    number_of_bonuses   int,

    CONSTRAINT fk_account
        FOREIGN KEY (account_id) REFERENCES public.personal_account(id)
);
