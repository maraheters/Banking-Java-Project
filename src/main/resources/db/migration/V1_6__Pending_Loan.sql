CREATE TABLE public.pending_loan (
    id                  serial             PRIMARY KEY,
    account_id          bigint             NOT NULL,
    principal_amount    numeric(19, 4)     NOT NULL,
    interest_rate       numeric(19, 4)     NOT NULL,
    length_in_months    int                NOT NULL,
    requested_at        timestamp          NOT NULL DEFAULT now(),
    status              varchar            NOT NULL,

     CONSTRAINT fk_account
         FOREIGN KEY (account_id) REFERENCES public.account(id) ON DELETE CASCADE
);