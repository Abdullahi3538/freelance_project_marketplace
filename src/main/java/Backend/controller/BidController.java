package Backend.controller;

import Backend.dto.Biddto.BidRequestDTO;
import Backend.dto.Biddto.BidResponseDTO;
import Backend.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    // Only FREELANCERS can submit a bid
    @PostMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<BidResponseDTO> createBid(
            @Valid @RequestBody BidRequestDTO request) {
        return new ResponseEntity<>(bidService.createBid(request), HttpStatus.CREATED);
    }

    // CLIENTs, FREELANCERs and ADMINs can view bids
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BidResponseDTO>> getAllBids() {
        return ResponseEntity.ok(bidService.getAllBids());
    }

    // Get bids related to the logged-in user
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BidResponseDTO>> getMyBids() {
        return ResponseEntity.ok(bidService.getMyBids());
    }

    // Only CLIENTS can accept a bid on their own project
    @PutMapping("/{id}/accept")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<BidResponseDTO> acceptBid(@PathVariable Long id) {
        return ResponseEntity.ok(bidService.acceptBid(id));
    }

    // Only CLIENTS can reject a bid on their own project
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<BidResponseDTO> rejectBid(@PathVariable Long id) {
        return ResponseEntity.ok(bidService.rejectBid(id));
    }
}
