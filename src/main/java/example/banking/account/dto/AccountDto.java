package example.banking.account.dto;

import example.banking.account.types.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    protected Long id;
    protected String IBAN;
    protected BigDecimal balance;
    protected AccountStatus status;
    protected Long bankId;
    protected LocalDateTime createdAt;
}
