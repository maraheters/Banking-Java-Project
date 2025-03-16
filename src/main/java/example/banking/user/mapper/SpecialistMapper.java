package example.banking.user.mapper;

import example.banking.user.dto.specialist.SpecialistResponseDto;
import example.banking.user.entity.Specialist;

public class SpecialistMapper {

    public static SpecialistResponseDto toResponseDto(Specialist specialist) {
        var dto = specialist.toDto();

        return new SpecialistResponseDto(
                dto.getId(),
                dto.getEnterpriseId(),
                dto.getName(),
                dto.getEmail()
        );
    }
}
