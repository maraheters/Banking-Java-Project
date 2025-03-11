package example.banking.account.dto;

import example.banking.account.types.AccountStatus;
import example.banking.account.types.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String IBAN;
    private BigDecimal balance;
    private AccountStatus status;
    private AccountType type;
    private Long holderId;
    private Long bankId;
    private LocalDateTime dateCreated;
}
