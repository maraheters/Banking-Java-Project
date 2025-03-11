package example.banking.enterprise.dto;

import lombok.Data;

@Data
public class EnterpriseRequestDto {
    private Long bankId;
    private String type;
    private String legalName;
    private String unp;
    private String legalAddress;
}
