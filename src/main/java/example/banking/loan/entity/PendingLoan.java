package example.banking.loan.entity;

import example.banking.contracts.PendingEntityStatus;
import example.banking.exception.BadRequestException;
import example.banking.loan.dto.PendingLoanDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PendingLoan {
    @Getter
    private Long id;
    @Getter
    private Long accountId;
    private BigDecimal principalAmount;
    private BigDecimal interestRate; //in decimal form, eg 0.08 = 8%
    private Integer lengthInMonths;
    private PendingEntityStatus status;
    private LocalDate requestedAt;

    public static PendingLoan create(
            Long accountId, BigDecimal principalAmount, BigDecimal interestRate, Integer length) {
        var loan = new PendingLoan();

        loan.accountId = accountId;
        loan.principalAmount = principalAmount;
        loan.interestRate = interestRate;
        loan.lengthInMonths = length;
        loan.status = PendingEntityStatus.PENDING;
        loan.requestedAt = LocalDate.now();

        return loan;
    }

    public static PendingLoan fromDto(PendingLoanDto l) {
        return new PendingLoan(
                l.getId(),
                l.getAccountId(),
                l.getPrincipalAmount(),
                l.getInterestRate(),
                l.getLengthInMonths(),
                l.getStatus(),
                l.getRequestedAt());
    }

    public PendingLoanDto toDto() {
        return new PendingLoanDto(
                id, accountId, principalAmount, interestRate, lengthInMonths, status, requestedAt);
    }

    public void setApproved() {
        checkPending();
        status = PendingEntityStatus.APPROVED;
    }

    public void setRejected() {
        checkPending();
        status = PendingEntityStatus.REJECTED;
    }

    private void checkPending() {
        if (!status.equals(PendingEntityStatus.PENDING)) {
            throw new BadRequestException("Status must be pending to change its state");
        }
    }

}
