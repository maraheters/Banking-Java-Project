WITH inserted_account AS (
    INSERT INTO account (iban, status, balance, created_at, bank_id)
        VALUES ('1234', 'ACTIVE', 8000.000, now(), 1)
        RETURNING id
)
INSERT INTO personal_account(id, holder_id)
VALUES ( (SELECT id FROM inserted_account), 1 )
RETURNING id;


WITH inserted_account AS (
    INSERT INTO account (iban, status, balance, created_at, bank_id)
        VALUES ('47474', 'ACTIVE', 8000.000, now(), 2)
        RETURNING id
)
INSERT INTO personal_account(id, holder_id)
VALUES ( (SELECT id FROM inserted_account), 2 )
RETURNING id;