package example.banking.security.controller;

import example.banking.security.service.ClientsAuthService;
import example.banking.user.dto.client.RegisterClientRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients/auth")
public class ClientAuthController {

    private final ClientsAuthService service;

    @Autowired
    public ClientAuthController(ClientsAuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(
            @RequestBody RegisterClientRequestDto requestDto) {

        return ResponseEntity.ok(
            service.register(requestDto)
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam("id") Long id) {
        service.verify(id);

        return ResponseEntity.ok().build();
    }
}
