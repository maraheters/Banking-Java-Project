package example.banking.user.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
