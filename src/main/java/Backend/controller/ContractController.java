package Backend.controller;

import Backend.dto.Contractdto.ContractRequestDto;
import Backend.dto.Contractdto.ContractResponseDto;
import Backend.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    // Only CLIENTS can create a contract (when accepting a bid)
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ContractResponseDto> createContract(
            @Valid @RequestBody ContractRequestDto request) {
        return new ResponseEntity<>(contractService.createContract(request), HttpStatus.CREATED);
    }

    // Both CLIENT and FREELANCER (and ADMIN) can view contracts
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ContractResponseDto>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    // Get a single contract by id
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ContractResponseDto> getContractById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    // Get contracts related to the logged-in user
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ContractResponseDto>> getMyContracts() {
        return ResponseEntity.ok(contractService.getMyContracts());
    }

    // CLIENT marks the contract as COMPLETED
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ContractResponseDto> completeContract(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.completeContract(id));
    }
}
