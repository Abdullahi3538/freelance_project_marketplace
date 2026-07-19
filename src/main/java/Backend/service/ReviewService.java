package Backend.service;

import Backend.Enmu.ContractStatus;
import Backend.dto.Review.ReviewRequestDto;
import Backend.dto.Review.ReviewResponseDto;
import Backend.entity.Auth.User;
import Backend.entity.Contract.Contract;
import Backend.entity.Review.Review;
import Backend.exception.ResourceNotFoundException;
import Backend.repository.ContractRepository;
import Backend.repository.ReviewRepository;
import Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    public ReviewResponseDto createReview(ReviewRequestDto request) {
        // 1. Find the contract
        Contract contract = contractRepository.findById(request.getContractId())
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));

        // 2. The reviewer is whoever is logged in (CLIENT or FREELANCER)
        User reviewer = getCurrentUser();

        // 3. Verify the reviewer is actually part of this contract
        boolean isClient     = contract.getClient().getId().equals(reviewer.getId());
        boolean isFreelancer = contract.getFreelancer().getId().equals(reviewer.getId());

        if (!isClient && !isFreelancer) {
            throw new RuntimeException("You are not part of this contract and cannot leave a review");
        }

        // 4. Auto-determine the reviewee from the contract
        //    CLIENT reviews the FREELANCER, and FREELANCER reviews the CLIENT
        User reviewee = isClient ? contract.getFreelancer() : contract.getClient();

        // 5. Prevent duplicate reviews — one review per person per contract
        boolean alreadyReviewed = reviewRepository.findAll().stream()
                .anyMatch(r -> r.getContract().getId().equals(contract.getId())
                            && r.getReviewer().getId().equals(reviewer.getId()));
        if (alreadyReviewed) {
            throw new RuntimeException("You have already reviewed this contract");
        }

        // 6. Build and save the review
        Review review = new Review();
        review.setContract(contract);
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        return mapToResponse(review);
    }

    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll()
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

    private ReviewResponseDto mapToResponse(Review review) {
        ReviewResponseDto response = new ReviewResponseDto();
        response.setId(review.getId());
        response.setContractId(review.getContract().getId());
        response.setProjectTitle(review.getContract().getBid().getProject().getTitle());
        response.setReviewerName(review.getReviewer().getFullName());
        response.setRevieweeName(review.getReviewee().getFullName());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }
}
