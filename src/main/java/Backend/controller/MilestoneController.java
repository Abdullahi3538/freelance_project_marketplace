package Backend.controller;

import Backend.dto.Milestone.MilestoneRequestDto;
import Backend.dto.Milestone.MilestoneResponseDto;
import Backend.service.MilestoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/milestones")
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService milestoneService;

    // Only CLIENTS can create milestones inside a contract
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<MilestoneResponseDto> createMilestone(
            @Valid @RequestBody MilestoneRequestDto request) {
        return new ResponseEntity<>(milestoneService.createMilestone(request), HttpStatus.CREATED);
    }

    // Both CLIENT and FREELANCER can view milestones
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MilestoneResponseDto>> getAllMilestones() {
        return ResponseEntity.ok(milestoneService.getAllMilestones());
    }

    // Get milestones for a specific contract
    @GetMapping("/contract/{contractId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MilestoneResponseDto>> getMilestonesByContract(@PathVariable Long contractId) {
        return ResponseEntity.ok(milestoneService.getMilestonesByContract(contractId));
    }

    // FREELANCER marks their work as done on a milestone
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<MilestoneResponseDto> completeMilestone(@PathVariable Long id) {
        return ResponseEntity.ok(milestoneService.completeMilestone(id));
    }

    // CLIENT reviews the completed work and releases payment
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<MilestoneResponseDto> approveMilestone(@PathVariable Long id) {
        return ResponseEntity.ok(milestoneService.approveMilestone(id));
    }
}
