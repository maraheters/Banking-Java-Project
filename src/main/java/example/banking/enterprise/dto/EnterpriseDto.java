package example.banking.enterprise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseDto {
    private Long id;
    private Long bankId;
    private String type;
    private String legalName;
    private String unp;
    private String legalAddress;
}
