package example.banking.user.controller;

import example.banking.user.dto.specialist.SpecialistResponseDto;
import example.banking.user.mapper.SpecialistMapper;
import example.banking.user.service.SpecialistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/specialists")
public class SpecialistsController {

    private final SpecialistsService service;

    @Autowired
    public SpecialistsController(SpecialistsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SpecialistResponseDto>> getAll() {
        return ResponseEntity.ok(
                service.getAll().stream()
                        .map(SpecialistMapper::toResponseDto)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialistResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                SpecialistMapper.toResponseDto(service.getById(id))
        );
    }


}
