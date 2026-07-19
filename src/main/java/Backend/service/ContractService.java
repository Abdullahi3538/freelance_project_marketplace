package Backend.service;

import Backend.Enmu.BidStatus;
import Backend.Enmu.ContractStatus;
import Backend.dto.Contractdto.ContractRequestDto;
import Backend.dto.Contractdto.ContractResponseDto;
import Backend.entity.Auth.User;
import Backend.entity.Bid.Bid;
import Backend.entity.Contract.Contract;
import Backend.exception.ResourceNotFoundException;
import Backend.repository.BidRepository;
import Backend.repository.ContractRepository;
import Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    public ContractResponseDto createContract(ContractRequestDto request) {
        Bid bid = bidRepository.findById(request.getBidId())
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found"));

        // 1. Security Check: Only the project owner (CLIENT) can create a contract for this bid
        User currentClient = getCurrentUser();
        if (!bid.getProject().getClient().getId().equals(currentClient.getId())) {
            throw new RuntimeException("You do not own the project associated with this bid");
        }

        // 2. State Check: Contract can only be created if the bid was ACCEPTED
        if (bid.getStatus() != BidStatus.ACCEPTED) {
            throw new RuntimeException("Cannot create a contract for a bid that is not ACCEPTED");
        }

        // 3. Duplicate Check: Ensure a contract doesn't already exist for this bid
        boolean contractExists = contractRepository.findAll().stream()
                .anyMatch(c -> c.getBid().getId().equals(bid.getId()));
        if (contractExists) {
            throw new RuntimeException("A contract has already been created for this bid");
        }

        Contract contract = new Contract();
        contract.setBid(bid);
        contract.setClient(bid.getProject().getClient());
        contract.setFreelancer(bid.getFreelancer());
        contract.setStartDate(request.getStartDate());
        contract.setEndDate(request.getEndDate());
        contract.setTotalAmount(request.getTotalAmount());

        contractRepository.save(contract);

        return mapToResponse(contract);
    }

    public ContractResponseDto getContractById(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));
        return mapToResponse(contract);
    }

    public ContractResponseDto completeContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));
        User currentUser = getCurrentUser();
        if (!contract.getClient().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only the client can complete this contract");
        }
        contract.setStatus(ContractStatus.COMPLETED);
        contractRepository.save(contract);
        return mapToResponse(contract);
    }

    public List<ContractResponseDto> getAllContracts() {
        return contractRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ContractResponseDto> getMyContracts() {
        User currentUser = getCurrentUser();
        List<Contract> contracts;
        if (currentUser.getRole().name().equals("FREELANCER")) {
            contracts = contractRepository.findByFreelancerId(currentUser.getId());
        } else if (currentUser.getRole().name().equals("CLIENT")) {
            contracts = contractRepository.findByClientId(currentUser.getId());
        } else {
            contracts = contractRepository.findAll();
        }
        return contracts.stream().map(this::mapToResponse).toList();
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private ContractResponseDto mapToResponse(Contract contract) {
        ContractResponseDto response = new ContractResponseDto();

        response.setId(contract.getId());
        response.setClientName(contract.getClient().getFullName());
        response.setFreelancerName(contract.getFreelancer().getFullName());
        response.setProjectTitle(contract.getBid().getProject().getTitle());
        response.setStartDate(contract.getStartDate());
        response.setEndDate(contract.getEndDate());
        response.setTotalAmount(contract.getTotalAmount());
        response.setStatus(contract.getStatus());
        return response;
    }
}
