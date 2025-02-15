CREATE TABLE public.banking_user (
     id SERIAL PRIMARY KEY ,
     name VARCHAR(100) NOT NULL,
     phone_number VARCHAR(15),
     passport_number VARCHAR(20),
     identification_number VARCHAR(20),
     email VARCHAR(100) NOT NULL UNIQUE
);

ALTER TABLE public.account
    ADD CONSTRAINT fk_banking_user
        FOREIGN KEY (holder_id) REFERENCES public.banking_user(id);