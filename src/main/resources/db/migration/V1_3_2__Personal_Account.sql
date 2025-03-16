CREATE TABLE public.personal_account (
    id              BIGINT          PRIMARY KEY REFERENCES account(id) ON DELETE CASCADE,
    holder_id       BIGINT          NOT NULL,

    CONSTRAINT fk_holder
        FOREIGN KEY (holder_id) REFERENCES public.client(id)
);