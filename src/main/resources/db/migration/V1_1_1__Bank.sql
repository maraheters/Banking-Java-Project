CREATE TABLE public.bank (
    id          SERIAL          PRIMARY KEY ,
    name        VARCHAR(255)    NOT NULL,
    bic         VARCHAR(25)     NOT NULL UNIQUE ,
    address     TEXT
);