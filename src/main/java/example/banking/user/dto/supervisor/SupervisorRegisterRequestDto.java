package example.banking.user.dto.supervisor;

import lombok.Data;

import java.util.List;

@Data
public class SupervisorRegisterRequestDto {
    private String name;
    private String email;
    private String password;
    private Long companyId;
    private List<String> roles;
}
