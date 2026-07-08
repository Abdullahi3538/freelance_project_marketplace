package Backend.Controller;

import Backend.dto.Biddto.BidRequestDTO;
import Backend.dto.Biddto.BidResponseDTO;
import Backend.service.BidService;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody BidRequestDTO request) {

        return bidService.createBid(request);
    }

}

