package example.banking.user.entity;

import example.banking.account.entity.Account;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private Long id;
    private String name;
    private String phoneNumber;
    private String passportNumber;
    private String identificationNumber;
    private String email;
    private List<Account> accounts = new ArrayList<>();

    public static User create(
            String name,
            String phoneNumber,
            String passportNumber,
            String identificationNumber,
            String email) {

        User user = new User();
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setPassportNumber(passportNumber);
        user.setIdentificationNumber(identificationNumber);
        user.setEmail(email);

        return user;
    }

}


