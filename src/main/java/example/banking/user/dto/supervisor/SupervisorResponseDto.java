package example.banking.user.dto.supervisor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorResponseDto {
    protected Long id;
    protected String name;
    protected String email;
    private List<String> roles;
}
