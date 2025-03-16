CREATE TABLE public.salary_project (
    id              SERIAL          PRIMARY KEY,
    enterprise_id   BIGINT          NOT NULL,
    bank_id         BIGINT          NOT NULL,
    created_at      TIMESTAMP       NOT NULL,
    status          VARCHAR(50)     NOT NULL,

    CONSTRAINT fk_enterprise
        FOREIGN KEY (enterprise_id) REFERENCES public.enterprise(id),

    CONSTRAINT fk_bank
        FOREIGN KEY (bank_id) REFERENCES public.bank(id)
);