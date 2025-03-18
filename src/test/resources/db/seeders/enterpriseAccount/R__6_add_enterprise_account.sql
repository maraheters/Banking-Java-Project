WITH inserted_account AS (
    INSERT INTO account (iban, status, balance, created_at, bank_id)
        VALUES ('1234', 'ACTIVE', 8000.000, now(), 1)
        RETURNING id
)
INSERT INTO enterprise_account(id, enterprise_id, specialist_id)
VALUES ( (SELECT id FROM inserted_account), 1, 100 )
RETURNING id;


WITH inserted_account AS (
    INSERT INTO account (iban, status, balance, created_at, bank_id)
        VALUES ('2123312', 'ACTIVE', 8000.000, now(), 1)
        RETURNING id
)
INSERT INTO enterprise_account(id, enterprise_id, specialist_id)
VALUES ( (SELECT id FROM inserted_account), 1, 100 )
RETURNING id;