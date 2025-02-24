package example.banking.user.entity;

import example.banking.user.dto.client.ClientDto;
import example.banking.user.roles.ClientRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Client extends User {
    private Long userId;
    private String phoneNumber;
    private String passportNumber;
    private String identificationNumber;
    private List<ClientRole> roles;
    private Boolean isVerified;

    public static Client register(
            String name,
            String phoneNumber,
            String passportNumber,
            String identificationNumber,
            String email,
            String passwordHash,
            List<ClientRole> roles) {

        Client client = new Client();
        client.name = name;
        client.phoneNumber = phoneNumber;
        client.passportNumber = passportNumber;
        client.identificationNumber = identificationNumber;
        client.email = email;
        client.passwordHash = passwordHash;
        client.roles = roles;
        client.isVerified = false;

        return client;
    }

    public static Client fromDto(ClientDto dto) {
        Client client = new Client();
        client.id = dto.getId();
        client.userId = dto.getUserId();
        client.name = dto.getName();
        client.passwordHash = dto.getPasswordHash();
        client.email = dto.getEmail();
        client.phoneNumber = dto.getPhoneNumber();
        client.passportNumber = dto.getPassportNumber();
        client.identificationNumber = dto.getIdentificationNumber();
        client.roles = dto.getRoles();
        client.isVerified = dto.getIsVerified();
        return client;
    }

    public ClientDto toDto() {
        return new ClientDto(
            id, userId, name, email, passwordHash, phoneNumber, passportNumber, identificationNumber, roles, isVerified);
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified() {
        isVerified = true;
    }

}


