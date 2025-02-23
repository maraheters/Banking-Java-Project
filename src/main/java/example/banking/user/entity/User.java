package example.banking.user.entity;

import lombok.Getter;

public class User {
    @Getter
    protected Long id;
    protected String name;
    protected String email;
    protected String passwordHash;
}
