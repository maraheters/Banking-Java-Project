package example.banking.user.mapper;

import example.banking.user.dto.supervisor.SupervisorDto;
import example.banking.user.dto.supervisor.SupervisorResponseDto;
import example.banking.user.entity.Supervisor;

public class SupervisorMapper {

    public static SupervisorResponseDto toResponseDto(Supervisor supervisor) {
        var d = supervisor.toDto();
        return toResponseDto(d);
    }

    public static SupervisorResponseDto toResponseDto(SupervisorDto s) {
        return new SupervisorResponseDto(
                s.getId(),
                s.getName(),
                s.getEmail(),
                s.getRoles().stream()
                        .map(Enum::toString)
                        .toList()
        );
    }
}
