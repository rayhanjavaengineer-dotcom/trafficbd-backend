
package com.trafficbd.backend;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestController
@RequestMapping("/api")
public class TrafficReportController {

    private final TrafficReportRepository repository;

    public TrafficReportController(
            TrafficReportRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "application", "TrafficBD-360",
            "status", "UP"
        );
    }

    @PostMapping("/reports")
    public ResponseEntity<TrafficReport> createReport(
            @Valid @RequestBody TrafficReport report) {
        TrafficReport saved = repository.save(report);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @GetMapping("/reports")
    public List<TrafficReport> getReports() {
        return repository.findAll();
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<TrafficReport> getReport(
            @PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": "
                        + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid report data");

        return ResponseEntity.badRequest()
                .body(Map.of("error", message));
    }
}
