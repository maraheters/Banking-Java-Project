CREATE TABLE public.supervisor (
    id          INTEGER         PRIMARY KEY REFERENCES public.user(id) ON DELETE CASCADE
);

CREATE TABLE public.supervisor_role (
    id          SERIAL          PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL UNIQUE
);

CREATE TABLE public.supervisor_role_supervisor (
    role_id                     INTEGER NOT NULL,
    supervisor_id               INTEGER NOT NULL,

    PRIMARY KEY (role_id, supervisor_id),
    FOREIGN KEY (role_id)           REFERENCES public.supervisor_role(id),
    FOREIGN KEY (supervisor_id)     REFERENCES public.supervisor(id)        ON DELETE CASCADE
);


INSERT INTO public.supervisor_role(name)
VALUES ('OPERATOR'), ('MANAGER'), ('COMPANY_SPECIALIST'), ('ADMINISTRATOR')