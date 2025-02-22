package example.banking.loan.mapper;

import example.banking.loan.dto.LoanResponseDto;
import example.banking.loan.entity.Loan;

public class LoanMapper {

    public static LoanResponseDto toResponseDto(Loan l) {
        var d = l.toDto();

        return new LoanResponseDto(
            d.getId(), d.getAccountId(), d.getPrincipalAmount(), d.getPaidAmount(), d.getInterestRate(),
            d.getLengthInMonths(), d.getStatus(), d.getCreatedAt(), d.getLastPayment());
    }
}
