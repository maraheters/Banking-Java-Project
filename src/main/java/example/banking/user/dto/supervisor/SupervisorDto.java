package example.banking.user.dto.supervisor;

import example.banking.user.roles.SupervisorRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorDto {
    protected Long id;
    protected String name;
    protected String email;
    protected String passwordHash;
    private Long userId;
    private List<SupervisorRole> roles;
}
