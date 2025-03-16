package example.banking.user.entity;

import example.banking.user.dto.specialist.SpecialistDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Specialist extends User {

    private Long userId;

    private Long enterpriseId;

    public static Specialist register(
            String name,
            String email,
            String passwordHash,
            Long enterpriseId) {

        var specialist = new Specialist();
        specialist.name = name;
        specialist.email = email;
        specialist.passwordHash = passwordHash;
        specialist.enterpriseId = enterpriseId;
        specialist.userId = null;

        return specialist;
    }

    public SpecialistDto toDto() {
        return new SpecialistDto(
                id, userId, enterpriseId, name, email, passwordHash
        );
    }

    public static Specialist fromDto(SpecialistDto dto) {
        var specialist = new Specialist();
        specialist.id = dto.getId();
        specialist.userId = dto.getUserId();
        specialist.name = dto.getName();
        specialist.email = dto.getEmail();
        specialist.enterpriseId = dto.getEnterpriseId();
        specialist.passwordHash = dto.getPasswordHash();

        return specialist;
    }
}
