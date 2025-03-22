CREATE TABLE public.pending_loan (
    id                  SERIAL              PRIMARY KEY,
    account_id          INTEGER             NOT NULL,
    principal_amount    NUMERIC(19, 4)      NOT NULL,
    interest_rate       NUMERIC(19, 4)      NOT NULL,
    length_in_months    INTEGER             NOT NULL,
    requested_at        TIMESTAMP           NOT NULL DEFAULT now(),
    status              VARCHAR             NOT NULL,

     CONSTRAINT fk_account
         FOREIGN KEY (account_id) REFERENCES public.account(id) ON DELETE CASCADE
);