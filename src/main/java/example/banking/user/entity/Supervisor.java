package example.banking.user.entity;

import example.banking.user.dto.supervisor.SupervisorDto;
import example.banking.user.roles.SupervisorRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Supervisor extends User {

    private List<SupervisorRole> roles;

    public static Supervisor register(
            String name, String email, String passwordHash, List<SupervisorRole> roles) {
        var s = new Supervisor();
        s.name = name;
        s.email = email;
        s.passwordHash = passwordHash;
        s.roles = roles;
        return s;
    }

    public static Supervisor fromDto(SupervisorDto dto) {
        var s = new Supervisor();
        s.id = dto.getId();
        s.name = dto.getName();
        s.email = dto.getEmail();
        s.passwordHash = dto.getPasswordHash();
        s.roles = dto.getRoles();
        return s;
    }

    public SupervisorDto toDto() {
        return new SupervisorDto(
                id, name, email, passwordHash, roles);
    }
}
