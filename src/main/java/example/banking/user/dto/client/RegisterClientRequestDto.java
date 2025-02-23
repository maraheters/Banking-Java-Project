package example.banking.user.dto.client;

import lombok.Data;

@Data
public class RegisterClientRequestDto {
    private String name;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private String passportNumber;
    private String identificationNumber;
}
