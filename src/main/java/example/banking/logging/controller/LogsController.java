package example.banking.logging.controller;

import example.banking.logging.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
@PreAuthorize("hasAuthority('ADMINISTRATOR')")
public class LogsController {

    private final LogService service;

    @Autowired
    public LogsController(LogService service) {
        this.service = service;
    }

    @GetMapping("/user-activity")
    public ResponseEntity<byte[]> getLogFile() {
        var logFile = service.getLogFile();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=user-activity.log");
        headers.add("Content-Type", "application/octet-stream");

        return ResponseEntity.ok()
                .headers(headers)
                .body(logFile);
    }
}
