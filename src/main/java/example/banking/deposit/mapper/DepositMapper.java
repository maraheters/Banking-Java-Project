package example.banking.deposit.mapper;

import example.banking.deposit.dto.DepositResponseDto;
import example.banking.deposit.entity.Deposit;
import org.springframework.stereotype.Component;

@Component
public class DepositMapper {
    private DepositMapper() {}

    public static DepositResponseDto toResponseDto(Deposit deposit) {
        var d = deposit.toDto();

        return new DepositResponseDto(
            d.getId(), d.getMinimum(), d.getBonus(), d.getStatus(), d.getDateCreated(), d.getLastBonusDate(),
            d.getNumberOfBonusesYet(), d.getLengthInMonths(), d.getAccountId(), d.getInterestRate());
    }
}
