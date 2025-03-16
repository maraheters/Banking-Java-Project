package example.banking.user.dto.specialist;

import lombok.Data;

@Data
public class SpecialistRegisterRequestDto {
    private String name;
    private String email;
    private String password;
    private Long enterpriseId;
}
