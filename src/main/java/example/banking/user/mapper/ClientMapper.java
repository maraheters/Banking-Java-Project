package example.banking.user.mapper;

import example.banking.user.dto.client.ClientDto;
import example.banking.user.dto.client.ClientResponseDto;
import example.banking.user.dto.client.PendingClientResponseDto;
import example.banking.user.entity.Client;
import example.banking.user.entity.PendingClient;

public class ClientMapper {

    public static ClientResponseDto toResponseDto(Client client) {
        return toResponseDto(client.toDto());
    }

    public static ClientResponseDto toResponseDto(ClientDto d) {
        return new ClientResponseDto(
                d.getId(),
                d.getName(),
                d.getEmail(),
                d.getPhoneNumber(),
                d.getPassportNumber(),
                d.getIdentificationNumber(),
                d.getRoles().stream().map(Enum::toString).toList()
        );
    }

    public static PendingClientResponseDto toPendingClientResponseDto(PendingClient client) {
        var d = client.toDto();
        return new PendingClientResponseDto(
                d.getId(),
                d.getName(),
                d.getEmail(),
                d.getPhoneNumber(),
                d.getPassportNumber(),
                d.getIdentificationNumber(),
                d.getRequestedAt(),
                d.getStatus()
        );
    }
}
