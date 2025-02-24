package example.banking.user.mapper;

import example.banking.user.dto.client.ClientDto;
import example.banking.user.dto.client.ClientResponseDto;
import example.banking.user.entity.Client;

public class ClientMapper {

    public static ClientResponseDto toResponseDto(Client client) {
        return toResponseDto(client.toDto());
    }

    public static ClientResponseDto toResponseDto(ClientDto d) {
        return new ClientResponseDto(
                d.getId(),
                d.getUserId(),
                d.getName(),
                d.getEmail(),
                d.getPasswordHash(),
                d.getPhoneNumber(),
                d.getPassportNumber(),
                d.getIdentificationNumber(),
                d.getRoles().stream().map(Enum::toString).toList());
    }
}
