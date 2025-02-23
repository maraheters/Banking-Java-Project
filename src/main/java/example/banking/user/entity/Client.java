package example.banking.user.entity;

import example.banking.user.dto.client.ClientDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Client extends User {
    private Long userId;
    private String phoneNumber;
    private String passportNumber;
    private String identificationNumber;

    public static Client register(
            String name,
            String phoneNumber,
            String passportNumber,
            String identificationNumber,
            String email,
            String passwordHash) {

        Client client = new Client();
        client.name = name;
        client.phoneNumber = phoneNumber;
        client.passportNumber = passportNumber;
        client.identificationNumber = identificationNumber;
        client.email = email;
        client.passwordHash = passwordHash;

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
        return client;
    }

    public ClientDto toDto() {
        return new ClientDto(
            id, userId, name, email, passwordHash, phoneNumber, passportNumber, identificationNumber);
    }

}


