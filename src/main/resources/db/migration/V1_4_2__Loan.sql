CREATE TABLE public.loan (
     id                SERIAL             PRIMARY KEY,
     account_id        INTEGER            NOT NULL,
     principal_amount  NUMERIC(19, 4)     NOT NULL,
     paid_amount       NUMERIC(19, 4)     NOT NULL,
     interest_rate     NUMERIC(19, 4)     NOT NULL,
     length_in_months  INTEGER            NOT NULL,
     status            VARCHAR(50)        NOT NULL,
     created_at        TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
     last_payment      TIMESTAMP,

     CONSTRAINT fk_account
         FOREIGN KEY (account_id) REFERENCES public.personal_account(id)
);