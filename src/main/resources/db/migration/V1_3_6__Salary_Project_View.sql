CREATE VIEW salary_project_view AS
    SELECT
        sp.*,
        ea.specialist_id,
        COALESCE(SUM(sa.salary), 0) AS total_salary
    FROM salary_project sp
    LEFT JOIN salary_account sa ON sa.salary_project_id = sp.id
    LEFT JOIN enterprise_account ea ON sp.account_id = ea.id
GROUP BY sp.id, specialist_id;
