package example.banking.bank.dto;

import lombok.Data;

@Data
public class BankRequestDto {
    private String name;
    private String bic;
    private String address;
}
