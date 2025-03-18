package example.banking.salary.controller;

import example.banking.salary.dto.SalaryProjectRequestDto;
import example.banking.salary.dto.SalaryProjectResponseDto;
import example.banking.salary.mapper.SalaryProjectMapper;
import example.banking.salary.service.SalaryProjectService;
import example.banking.salary.types.SalaryProjectStatus;
import example.banking.security.BankingUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary-projects")
public class SalaryProjectController {

    private final SalaryProjectService salaryProjectService;

    @Autowired
    public SalaryProjectController(SalaryProjectService salaryProjectService) {
        this.salaryProjectService = salaryProjectService;
    }

    @PostMapping
    @PreAuthorize("""
        hasAuthority('SPECIALIST') &&
        @enterpriseAccountsService.validateOwner(#dto.accountId, authentication.principal)
    """)
    public ResponseEntity<Long> create(
            @RequestBody SalaryProjectRequestDto dto,
            @AuthenticationPrincipal BankingUserDetails userDetails) {

        return ResponseEntity.ok(
            salaryProjectService.create(userDetails, dto)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<SalaryProjectResponseDto>> getAll(
            @RequestParam(value = "status", required = false) SalaryProjectStatus status
    ) {

        var projectsStream = salaryProjectService.getAll().stream();

        if (status != null) {
            projectsStream = projectsStream.filter(p -> p.getStatus().equals(status));
        }

        return ResponseEntity.ok(
                projectsStream
                        .map(SalaryProjectMapper::toResponseDto)
                        .toList()
        );
    }

//    @GetMapping
//    @PreAuthorize("hasAuthority('SPECIALIST')")
//    public ResponseEntity<List<SalaryProjectResponseDto>> getAllBySpecialist(
//
//    ) {
//
//        var projectsStream = salaryProjectService.getAll().stream();
//
//        if (status != null) {
//            projectsStream = projectsStream.filter(p -> p.getStatus().equals(status));
//        }
//
//        return ResponseEntity.ok(
//                projectsStream
//                        .map(SalaryProjectMapper::toResponseDto)
//                        .toList()
//        );
//    }
}
