CREATE TABLE public.deposit
(
      id            BIGINT AUTO_INCREMENT PRIMARY KEY,
      balance       DECIMAL(19, 4) NOT NULL DEFAULT 0.00,
      status        VARCHAR(50) NOT NULL,
      date_created  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      account_id    BIGINT, --make not null later
      interest_rate DOUBLE NOT NULL,

--       FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);