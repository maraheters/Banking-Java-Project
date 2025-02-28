package example.banking.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponseDto {
    Long id;
    String email;
    String token;
    List<String> authorities;
}
