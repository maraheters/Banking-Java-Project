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
public class PersonalAccountResponseDto {
    private Long id;
    private String IBAN;
    private BigDecimal balance;
    private AccountStatus status;
    private Long holderId;
    private LocalDateTime createdAt;
}
