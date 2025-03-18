WITH inserted_user AS (
    INSERT INTO public.user(id, name, email, password_hash)
        VALUES (100, 'name1', 'specialist1@example.com', 'pword1')
        RETURNING id
)
INSERT INTO public.specialist (id, enterprise_id)
VALUES ( (SELECT id FROM inserted_user), 1 );


WITH inserted_user AS (
    INSERT INTO public.user(id, name, email, password_hash)
        VALUES (101, 'name2', 'specialist2@example.com', 'pword2')
        RETURNING id
)
INSERT INTO public.specialist (id, enterprise_id)
VALUES ( (SELECT id FROM inserted_user), 1 );