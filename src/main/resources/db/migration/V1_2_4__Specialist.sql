CREATE TABLE specialist (
    id              INTEGER          PRIMARY KEY REFERENCES public.user(id) ON DELETE CASCADE,
    enterprise_id   INTEGER         NOT NULL,

    CONSTRAINT fk_enterprise
        FOREIGN KEY (enterprise_id) REFERENCES  public.enterprise(id) ON DELETE CASCADE
);