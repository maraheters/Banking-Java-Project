CREATE TABLE public.personal_account (
    id              INTEGER         PRIMARY KEY REFERENCES account(id) ON DELETE CASCADE,
    holder_id       INTEGER         NOT NULL,

    CONSTRAINT fk_holder
        FOREIGN KEY (holder_id) REFERENCES public.client(id)
);