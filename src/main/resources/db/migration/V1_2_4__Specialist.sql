CREATE TABLE specialist (
    id              BIGINT          PRIMARY KEY REFERENCES public.user(id) ON DELETE CASCADE,
    enterprise_id   BIGINT          NOT NULL,

    CONSTRAINT fk_enterprise
        FOREIGN KEY (enterprise_id) REFERENCES  public.enterprise(id) ON DELETE CASCADE
);