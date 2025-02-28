package example.banking.user.dto.client;

import lombok.Data;

@Data
public class ClientRegisterRequestDto {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String passportNumber;
    private String identificationNumber;
}
