CREATE TABLE public.supervisor (
    id              SERIAL          PRIMARY KEY,
    user_id         BIGINT          NOT NULL,
    company_id      BIGINT,         -- intentionally nullable

    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES public.user(id)
            ON DELETE CASCADE
);

CREATE TABLE public.supervisor_role (
    id          SERIAL          PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL UNIQUE
);

CREATE TABLE public.supervisor_role_supervisor (
    role_id                         BIGINT NOT NULL,
    supervisor_id                   BIGINT NOT NULL,

    PRIMARY KEY (role_id, supervisor_id),
    FOREIGN KEY (role_id)           REFERENCES public.supervisor_role(id),
    FOREIGN KEY (supervisor_id)     REFERENCES public.supervisor(id)        ON DELETE CASCADE
);
