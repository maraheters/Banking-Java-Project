CREATE TABLE public.enterprise_account (
    id              BIGINT      PRIMARY KEY REFERENCES account(id) ON DELETE CASCADE,
    enterprise_id   BIGINT      NOT NULL,
    specialist_id   BIGINT      NOT NULL,

    CONSTRAINT fk_enterprise
        FOREIGN KEY (enterprise_id) REFERENCES public.enterprise(id),

    CONSTRAINT fk_specialist
        FOREIGN KEY (specialist_id) REFERENCES public.specialist(id)
);
