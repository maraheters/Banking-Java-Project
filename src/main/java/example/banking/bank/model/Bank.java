package example.banking.bank.model;

import example.banking.bank.dto.BankDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Bank {
    @Getter
    private Long id;
    private String name;
    private String bic;
    private String address;

    public static Bank create(String name, String bic, String address) {
        return new Bank(null, name, bic, address);
    }

    public BankDto toDto() {
        return new BankDto(id, name, bic, address);
    }

    public static Bank fromDto(BankDto dto) {
        return new Bank(dto.getId(), dto.getName(), dto.getBic(), dto.getAddress());
    }
}