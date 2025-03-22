CREATE TABLE public.salary_project (
    id              SERIAL          PRIMARY KEY,
    enterprise_id   INTEGER         NOT NULL,
    account_id      INTEGER         UNIQUE NOT NULL,
    created_at      TIMESTAMP       NOT NULL,
    status          VARCHAR(50)     NOT NULL,

    CONSTRAINT fk_enterprise
        FOREIGN KEY (enterprise_id) REFERENCES public.enterprise(id),

    CONSTRAINT fk_account
        FOREIGN KEY (account_id) REFERENCES public.enterprise_account(id)
);