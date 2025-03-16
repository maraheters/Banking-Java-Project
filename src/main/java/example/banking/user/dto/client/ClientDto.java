package example.banking.user.dto.client;

import example.banking.user.roles.ClientRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private String passportNumber;
    private String identificationNumber;
    private List<ClientRole> roles;
}
