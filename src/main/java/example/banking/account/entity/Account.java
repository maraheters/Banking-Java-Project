package example.banking.account.entity;

import example.banking.account.types.AccountStatus;
import example.banking.account.types.AccountType;
import example.banking.utils.IbanGenerator;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Account {
    private Long id;
    private String IBAN;
    private BigDecimal balance;
    private AccountStatus status;
    private AccountType type;
    private Long holderId;
    private LocalDateTime dateCreated;
    private List<Long> depositIds = new ArrayList<>();

    public static Account create(Long holderId, AccountType type) {
        var account = new Account();
        account.IBAN = IbanGenerator.Generate("BY");
        account.balance = BigDecimal.ZERO;
        account.status = AccountStatus.FROZEN;
        account.type = type;
        account.holderId = holderId;
        account.dateCreated = LocalDateTime.now();

        return account;
    }
}
