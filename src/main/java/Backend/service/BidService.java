package Backend.service;

import Backend.Enmu.BidStatus;
import Backend.Enmu.ProjectStatus;
import Backend.dto.Biddto.BidRequestDTO;
import Backend.dto.Biddto.BidResponseDTO;
import Backend.entity.Auth.User;
import Backend.entity.Bid.Bid;
import Backend.entity.Project.Project;
import Backend.exception.ResourceNotFoundException;
import Backend.repository.UserRepository;
import Backend.repository.BidRepository;
import Backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // FREELANCER creates a bid
    public BidResponseDTO createBid(BidRequestDTO request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new RuntimeException("Cannot bid on a project that is not OPEN");
        }

        User freelancer = getCurrentUser();

        // Check if freelancer already bid on this project
        boolean alreadyBid = bidRepository.findAll().stream()
                .anyMatch(b -> b.getProject().getId().equals(project.getId()) &&
                               b.getFreelancer().getId().equals(freelancer.getId()));
        
        if (alreadyBid) {
            throw new RuntimeException("You have already placed a bid on this project");
        }

        Bid bid = new Bid();
        bid.setBidAmount(request.getBidAmount());
        bid.setDeliveryDays(request.getDeliveryDays());
        bid.setProposal(request.getProposal());
        bid.setProject(project);
        bid.setFreelancer(freelancer);

        bidRepository.save(bid);

        return mapToResponse(bid);
    }

    // Get all bids
    public List<BidResponseDTO> getAllBids() {
        return bidRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get bids relevant to the logged-in user
    public List<BidResponseDTO> getMyBids() {
        User currentUser = getCurrentUser();
        List<Bid> bids;
        if (currentUser.getRole().name().equals("FREELANCER")) {
            bids = bidRepository.findByFreelancerId(currentUser.getId());
        } else if (currentUser.getRole().name().equals("CLIENT")) {
            bids = bidRepository.findByProjectClientId(currentUser.getId());
        } else {
            bids = bidRepository.findAll();
        }
        return bids.stream().map(this::mapToResponse).toList();
    }

    // CLIENT accepts a bid
    public BidResponseDTO acceptBid(Long bidId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found"));

        User currentClient = getCurrentUser();
        Project project = bid.getProject();

        // Verify the logged-in client owns the project
        if (!project.getClient().getId().equals(currentClient.getId())) {
            throw new RuntimeException("You do not own this project");
        }

        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new RuntimeException("This project is no longer open for bidding");
        }

        // Accept this bid
        bid.setStatus(BidStatus.ACCEPTED);
        bidRepository.save(bid);

        // Reject all other pending bids for this project
        List<Bid> otherBids = bidRepository.findAll().stream()
                .filter(b -> b.getProject().getId().equals(project.getId()) && !b.getId().equals(bidId))
                .toList();
        
        for (Bid otherBid : otherBids) {
            otherBid.setStatus(BidStatus.REJECTED);
            bidRepository.save(otherBid);
        }

        // Change project status to IN_PROGRESS
        project.setStatus(ProjectStatus.IN_PROGRESS);
        projectRepository.save(project);

        return mapToResponse(bid);
    }

    // CLIENT rejects a bid manually
    public BidResponseDTO rejectBid(Long bidId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found"));

        User currentClient = getCurrentUser();
        
        if (!bid.getProject().getClient().getId().equals(currentClient.getId())) {
            throw new RuntimeException("You do not own this project");
        }

        bid.setStatus(BidStatus.REJECTED);
        bidRepository.save(bid);

        return mapToResponse(bid);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private BidResponseDTO mapToResponse(Bid bid) {
        BidResponseDTO response = new BidResponseDTO();
        response.setId(bid.getId());
        response.setBidAmount(bid.getBidAmount());
        response.setDeliveryDays(bid.getDeliveryDays());
        response.setProposal(bid.getProposal());
        response.setStatus(bid.getStatus());
        response.setProjectTitle(bid.getProject().getTitle());
        response.setFreelancerName(bid.getFreelancer().getFullName());
        return response;
    }
}
