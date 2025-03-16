package example.banking.user.dto.specialist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialistDto {
    private Long id;
    private Long enterpriseId;
    private String name;
    private String email;
    private String passwordHash;
}
