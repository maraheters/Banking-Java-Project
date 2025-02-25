CREATE TABLE public.client (
    id                      SERIAL          PRIMARY KEY,
    user_id                 BIGINT          NOT NULL,
    phone_number            VARCHAR(255)    NOT NULL UNIQUE,
    passport_number         VARCHAR(255)    NOT NULL UNIQUE,
    identification_number   VARCHAR(255)    NOT NULL UNIQUE,

    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES public.user(id)
           ON DELETE CASCADE
);

CREATE TABLE public.client_role (
    id          SERIAL          PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL UNIQUE
);

CREATE TABLE public.client_role_client (
    role_id                         BIGINT NOT NULL,
    client_id                       BIGINT NOT NULL,

    PRIMARY KEY (role_id,  client_id),
    FOREIGN KEY (role_id)           REFERENCES public.client_role(id),
    FOREIGN KEY (client_id)         REFERENCES public.client(id)        ON DELETE CASCADE
);

ALTER TABLE public.account
    ADD CONSTRAINT fk_client
        FOREIGN KEY (holder_id) REFERENCES public.client(id);


INSERT INTO public.client_role(name)
VALUES ('BASIC');
