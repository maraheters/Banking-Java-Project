CREATE TABLE public.enterprise_account (
    id              INTEGER     PRIMARY KEY REFERENCES account(id) ON DELETE CASCADE,
    enterprise_id   INTEGER     NOT NULL,
    specialist_id   INTEGER     NOT NULL,

    CONSTRAINT fk_enterprise
        FOREIGN KEY (enterprise_id) REFERENCES public.enterprise(id),

    CONSTRAINT fk_specialist
        FOREIGN KEY (specialist_id) REFERENCES public.specialist(id)
);
