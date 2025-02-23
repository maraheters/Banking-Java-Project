package example.banking.user.dto.supervisor;

import lombok.Data;

import java.util.List;

@Data
public class SupervisorRegisterRequestDto {
    protected String name;
    protected String email;
    protected String passwordHash;
    private Long companyId;
    private List<String> roles;
}
