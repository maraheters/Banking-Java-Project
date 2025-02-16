CREATE TABLE public.deposit
(
    id                  serial primary key,
    balance             numeric(19, 4) default 0.00              not null,
    status              varchar(50)                              not null,
    date_created        timestamp      default CURRENT_TIMESTAMP not null,
    length_in_months    int                                      not null,
    account_id          bigint                                   not null,
    interest_rate       double precision                         not null
);