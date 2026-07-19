package Backend.service;

import Backend.Enmu.ContractStatus;
import Backend.Enmu.MilestoneStatus;
import Backend.dto.Milestone.MilestoneRequestDto;
import Backend.dto.Milestone.MilestoneResponseDto;
import Backend.entity.Auth.User;
import Backend.entity.Contract.Contract;
import Backend.entity.Milestone.Milestone;
import Backend.exception.ResourceNotFoundException;
import Backend.repository.ContractRepository;
import Backend.repository.MilestoneRepository;
import Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    // FREELANCER creates a milestone inside their active contract
    public MilestoneResponseDto createMilestone(MilestoneRequestDto request) {
        Contract contract = contractRepository.findById(request.getContractId())
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));

        // Security: only the freelancer who works on this contract can create milestones
        User currentUser = getCurrentUser();
        if (!contract.getFreelancer().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not the freelancer of this contract");
        }

        // State: can only add milestones to an ACTIVE contract
        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new RuntimeException("Cannot add milestones to a contract that is not ACTIVE");
        }

        // Budget check: total of all milestones must not exceed the contract's total amount
        List<Milestone> existingMilestones = milestoneRepository.findByContractId(contract.getId());
        double alreadyAllocated = existingMilestones.stream()
                .mapToDouble(Milestone::getAmount)
                .sum();
        double newTotal = alreadyAllocated + request.getAmount();
        if (newTotal > contract.getTotalAmount()) {
            double remaining = contract.getTotalAmount() - alreadyAllocated;
            throw new RuntimeException(
                String.format("Milestone amount exceeds contract budget. Remaining budget: $%.2f", remaining)
            );
        }

        Milestone milestone = new Milestone();
        milestone.setTitle(request.getTitle());
        milestone.setDescription(request.getDescription());
        milestone.setDueDate(request.getDueDate());
        milestone.setAmount(request.getAmount());
        milestone.setContract(contract);

        milestoneRepository.save(milestone);

        return mapToResponse(milestone);
    }

    // FREELANCER marks a milestone as COMPLETED (they finished the work)
    public MilestoneResponseDto completeMilestone(Long id) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone not found"));

        // Security: only the freelancer working on this contract can complete milestones
        User currentUser = getCurrentUser();
        if (!milestone.getContract().getFreelancer().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not the freelancer working on this contract");
        }

        // State: can only complete a PENDING milestone
        if (milestone.getStatus() != MilestoneStatus.PENDING) {
            throw new RuntimeException("Only PENDING milestones can be marked as COMPLETED");
        }

        milestone.setStatus(MilestoneStatus.COMPLETED);
        milestoneRepository.save(milestone);

        return mapToResponse(milestone);
    }

    // CLIENT approves the completed work and releases the payment
    public MilestoneResponseDto approveMilestone(Long id) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone not found"));

        // Security: only the client who owns this contract can approve milestones
        User currentUser = getCurrentUser();
        if (!milestone.getContract().getClient().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not the client of this contract");
        }

        // State: can only approve a COMPLETED milestone (work must be submitted first)
        if (milestone.getStatus() != MilestoneStatus.COMPLETED) {
            throw new RuntimeException("Only COMPLETED milestones can be approved and marked as PAID");
        }

        milestone.setStatus(MilestoneStatus.PAID);
        milestoneRepository.save(milestone);

        return mapToResponse(milestone);
    }

    // View all milestones — any authenticated user
    public List<MilestoneResponseDto> getAllMilestones() {
        return milestoneRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // View milestones for a specific contract
    public List<MilestoneResponseDto> getMilestonesByContract(Long contractId) {
        return milestoneRepository.findByContractId(contractId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private MilestoneResponseDto mapToResponse(Milestone milestone) {
        MilestoneResponseDto response = new MilestoneResponseDto();
        response.setId(milestone.getId());
        response.setContractId(milestone.getContract().getId());
        response.setTitle(milestone.getTitle());
        response.setDescription(milestone.getDescription());
        response.setDueDate(milestone.getDueDate());
        response.setAmount(milestone.getAmount());
        response.setStatus(milestone.getStatus());
        return response;
    }
}
