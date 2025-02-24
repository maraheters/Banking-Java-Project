package example.banking.user.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDto {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private String passportNumber;
    private String identificationNumber;
    private List<String> roles;
}
