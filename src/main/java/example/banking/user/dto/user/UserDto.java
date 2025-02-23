package example.banking.user.dto.user;

import lombok.Data;

@Data
public class UserDto {
    protected Long id;
    protected String name;
    protected String email;
    protected String passwordHash;
}
