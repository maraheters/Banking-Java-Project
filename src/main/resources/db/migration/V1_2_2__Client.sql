CREATE TABLE public.client (
    id                      BIGINT          PRIMARY KEY REFERENCES public.user(id) ON DELETE CASCADE,
    phone_number            VARCHAR(255)    NOT NULL UNIQUE,
    passport_number         VARCHAR(255)    NOT NULL UNIQUE,
    identification_number   VARCHAR(255)    NOT NULL UNIQUE
);

CREATE TABLE public.client_role (
    id          SERIAL          PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL UNIQUE
);

CREATE TABLE public.client_role_client (
    role_id         BIGINT          NOT NULL,
    client_id       BIGINT          NOT NULL,

    PRIMARY KEY (role_id,  client_id),
    FOREIGN KEY (role_id)           REFERENCES public.client_role(id),
    FOREIGN KEY (client_id)         REFERENCES public.client(id)        ON DELETE CASCADE
);

INSERT INTO public.client_role(name)
VALUES ('BASIC');
