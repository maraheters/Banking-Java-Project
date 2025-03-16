package example.banking.user.dto.specialist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialistResposneDto {
    private Long id;
    private Long userId;
    private Long enterpriseId;
    private String name;
    private String email;
}
