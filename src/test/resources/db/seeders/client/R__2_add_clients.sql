WITH
    inserted_user AS (
        INSERT INTO public.user(name, email, password_hash)
            VALUES ('name1', 'email1', 'password1')
            RETURNING id
    ),
    inserted_client AS (
        INSERT INTO public.client (id, phone_number, passport_number, identification_number)
            VALUES ((SELECT id FROM inserted_user), 'phone_number1', 'passport_number1', 'identification_number1')
            RETURNING id
    ),
    role_ids AS (
        SELECT id
        FROM public.client_role
        WHERE name IN ('BASIC')
    ),
    id AS (
        INSERT INTO public.client_role_client(role_id, client_id)
            SELECT id, (SELECT id FROM inserted_client)
            FROM role_ids
            RETURNING client_id
    )
SELECT * FROM id LIMIT 1;

WITH
    inserted_user AS (
        INSERT INTO public.user(name, email, password_hash)
            VALUES ('name2', 'email2', 'password2')
            RETURNING id
    ),
    inserted_client AS (
        INSERT INTO public.client (id, phone_number, passport_number, identification_number)
            VALUES ((SELECT id FROM inserted_user), 'phone_number2', 'passport_number2', 'identification_number2')
            RETURNING id
    ),
    role_ids AS (
        SELECT id
        FROM public.client_role
        WHERE name IN ('BASIC')
    ),
    id AS (
        INSERT INTO public.client_role_client(role_id, client_id)
            SELECT id, (SELECT id FROM inserted_client)
            FROM role_ids
            RETURNING client_id
    )
SELECT * FROM id LIMIT 1;