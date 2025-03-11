package example.banking.enterprise.model;

import example.banking.enterprise.dto.EnterpriseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Enterprise {
    @Getter
    private Long id;
    private Long bankId;
    private String type;
    private String legalName;
    private String unp;
    private String legalAddress;

    public static Enterprise create(
            String type, String legalName, String unp, Long bankId, String legalAddress) {

        return new Enterprise(
                null, bankId, type, legalName, unp, legalAddress
        );
    }

    public EnterpriseDto toDto() {
        return new EnterpriseDto(id, bankId, type, legalName, unp, legalAddress);
    }

    public static Enterprise fromDto(EnterpriseDto dto) {
        return new Enterprise(
                dto.getId(), dto.getBankId(), dto.getType(), dto.getLegalName(), dto.getUnp(), dto.getLegalAddress()
        );
    }
}
