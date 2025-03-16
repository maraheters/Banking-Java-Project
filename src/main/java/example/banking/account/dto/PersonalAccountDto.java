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
public class PersonalAccountDto {
    private Long id;
    private Long holderId;
    private String IBAN;
    private BigDecimal balance;
    private AccountStatus status;
    private Long bankId;
    private LocalDateTime createdAt;
}
