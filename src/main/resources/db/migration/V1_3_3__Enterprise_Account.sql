CREATE TABLE public.enterprise_account (
    id              BIGINT      PRIMARY KEY REFERENCES account(id) ON DELETE CASCADE,
    enterprise_id   BIGINT      UNIQUE NOT NULL,

    CONSTRAINT fk_enterprise
        FOREIGN KEY (enterprise_id) REFERENCES public.enterprise(id)
);
