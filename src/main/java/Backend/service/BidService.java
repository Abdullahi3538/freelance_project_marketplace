package Backend.service;


import Backend.dto.Biddto.BidRequestDTO;
import Backend.dto.Biddto.BidResponseDTO;
import Backend.entity.Auth.User;
import Backend.entity.Bid.Bid;
import Backend.entity.Project.Project;
import Backend.repository.UserRepository;
import Backend.repository.BidRepository;
import Backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public BidService(
            BidRepository bidRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository) {

        this.bidRepository = bidRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public BidResponseDTO createBid(
            BidRequestDTO request) {

        Project project = projectRepository.findById(
                request.getProjectId()
        ).orElseThrow(
                () -> new RuntimeException("Project not found")
        );

        User freelancer = userRepository.findById(
                request.getFreelancerId()
        ).orElseThrow(
                () -> new RuntimeException("Freelancer not found")
        );

        Bid bid = new Bid();

        bid.setBidAmount(request.getBidAmount());
        bid.setDeliveryDays(request.getDeliveryDays());
        bid.setProposal(request.getProposal());

        bid.setProject(project);
        bid.setFreelancer(freelancer);

        bidRepository.save(bid);

        BidResponseDTO response =
                new BidResponseDTO();

        response.setId(bid.getId());
        response.setBidAmount(bid.getBidAmount());
        response.setDeliveryDays(bid.getDeliveryDays());
        response.setProposal(bid.getProposal());

        response.setStatus(bid.getStatus());

        response.setProjectTitle(
                project.getTitle()
        );

        response.setFreelancerName(
                freelancer.getFullName()
        );

        return response;
    }

}