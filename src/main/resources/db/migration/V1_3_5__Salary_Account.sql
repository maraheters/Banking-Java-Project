CREATE TABLE public.salary_account (
    id                  INTEGER         PRIMARY KEY REFERENCES public.account(id) ON DELETE CASCADE,
    holder_id           INTEGER         NOT NULL,
    salary              NUMERIC(19,4)   NOT NULL,
    salary_project_id   INTEGER         NOT NULL,

    CONSTRAINT fk_holder
        FOREIGN KEY (holder_id) REFERENCES public.client(id),

    CONSTRAINT fk_salary_project
        FOREIGN KEY (salary_project_id) REFERENCES public.salary_project(id) ON DELETE CASCADE
)