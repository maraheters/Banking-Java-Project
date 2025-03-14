package example.banking.loan.mapper;

import example.banking.loan.dto.LoanResponseDto;
import example.banking.loan.dto.LoanTermDto;
import example.banking.loan.dto.PendingLoanResponseDto;
import example.banking.loan.entity.Loan;
import example.banking.loan.entity.PendingLoan;
import example.banking.loan.types.LoanTerm;

public class LoanMapper {

    public static LoanResponseDto toResponseDto(Loan l) {
        var d = l.toDto();

        return new LoanResponseDto(
            d.getId(), d.getAccountId(), d.getPrincipalAmount(), d.getPaidAmount(), d.getInterestRate(),
            d.getLengthInMonths(), d.getStatus(), d.getCreatedAt(), d.getLastPayment());
    }

    public static PendingLoanResponseDto toPendingLoanResponseDto(PendingLoan l) {
        var d = l.toDto();

        return new PendingLoanResponseDto(
                d.getId(), d.getAccountId(), d.getPrincipalAmount(), d.getInterestRate(),
                d.getLengthInMonths(), d.getStatus(), d.getRequestedAt()
        );
    }

    public static LoanTermDto toLoanTermDto(LoanTerm term) {
        return new LoanTermDto(
                term.name(), term.getInterestRate(), term.getMonths()
        );
    }
}
