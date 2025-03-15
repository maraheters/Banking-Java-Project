package example.banking.user.dto.client;

import example.banking.contracts.PendingEntityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingClientResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String passportNumber;
    private String identificationNumber;
    private LocalDateTime requestedAt;
    private PendingEntityStatus status;
}
