package example.banking.user.entity;

import example.banking.contracts.PendingEntityStatus;
import example.banking.exception.BadRequestException;
import example.banking.user.dto.client.PendingClientDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PendingClient {
    @Getter
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private String passportNumber;
    private String identificationNumber;
    private LocalDateTime requestedAt;
    private PendingEntityStatus status;


    public static PendingClient register(
            String name,
            String phoneNumber,
            String passportNumber,
            String identificationNumber,
            String email,
            String passwordHash) {

        var client = new PendingClient();
        client.name = name;
        client.email = email;
        client.passwordHash = passwordHash;
        client.phoneNumber = phoneNumber;
        client.passportNumber = passportNumber;
        client.identificationNumber = identificationNumber;
        client.requestedAt = LocalDateTime.now();
        client.status = PendingEntityStatus.PENDING;

        return client;
    }

    public PendingClientDto toDto() {
        return new PendingClientDto(
                id, name, email, passwordHash, phoneNumber, passportNumber,
                identificationNumber, requestedAt, status
        );
    }

    public static PendingClient fromDto(PendingClientDto dto) {
        return new PendingClient(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getPasswordHash(),
                dto.getPhoneNumber(),
                dto.getPassportNumber(),
                dto.getIdentificationNumber(),
                dto.getRequestedAt(),
                dto.getStatus()
        );
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
