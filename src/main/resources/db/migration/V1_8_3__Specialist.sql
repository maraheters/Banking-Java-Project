CREATE TABLE specialist (
    id              SERIAL          PRIMARY KEY,
    user_id         BIGINT          NOT NULL,
    enterprise_id   BIGINT          NOT NULL,

    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES public.user(id) ON DELETE CASCADE,

    CONSTRAINT fk_enterprise
        FOREIGN KEY (enterprise_id) REFERENCES  public.enterprise(id) ON DELETE CASCADE
);