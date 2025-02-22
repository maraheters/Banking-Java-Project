CREATE TABLE public.loan (
     id                serial             PRIMARY KEY,
     account_id        bigint             NOT NULL,
     principal_amount  numeric(19, 4)     NOT NULL,
     paid_amount       numeric(19, 4)     NOT NULL,
     interest_rate     numeric(19, 4)     NOT NULL,
     length_in_months  int                NOT NULL,
     status            varchar(50)        NOT NULL,
     created_at        timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP,
     last_payment      timestamp,

     CONSTRAINT fk_account
         FOREIGN KEY (account_id) REFERENCES public.account(id)
);