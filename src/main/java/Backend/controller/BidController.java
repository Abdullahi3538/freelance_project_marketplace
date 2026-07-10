package Backend.controller;

import Backend.dto.Biddto.BidRequestDTO;
import Backend.dto.Biddto.BidResponseDTO;
import Backend.service.BidService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@CrossOrigin("*")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping
    public BidResponseDTO createBid(
            @Valid @RequestBody BidRequestDTO request) {

        return bidService.createBid(request);
    }

    @GetMapping
    public List<BidResponseDTO> getAllBids() {
        return bidService.getAllBids();
    }

}

