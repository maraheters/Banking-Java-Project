package example.banking.user.mapper;

import example.banking.user.dto.specialist.SpecialistResposneDto;
import example.banking.user.entity.Specialist;

public class SpecialistMapper {

    public static SpecialistResposneDto toResponseDto(Specialist specialist) {
        var dto = specialist.toDto();

        return new SpecialistResposneDto(
                dto.getId(),
                dto.getUserId(),
                dto.getEnterpriseId(),
                dto.getName(),
                dto.getEmail()
        );
    }
}
