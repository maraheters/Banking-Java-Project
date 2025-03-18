INSERT INTO salary_project(enterprise_id, account_id, created_at, status)
VALUES (1, 1, now(), 'ACTIVE')
RETURNING id;

INSERT INTO salary_project(enterprise_id, account_id, created_at, status)
VALUES (2, 2, now(), 'ACTIVE')
RETURNING id