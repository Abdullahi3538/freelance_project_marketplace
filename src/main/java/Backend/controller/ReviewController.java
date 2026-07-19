package Backend.controller;

import Backend.dto.Review.ReviewRequestDto;
import Backend.dto.Review.ReviewResponseDto;
import Backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // Any authenticated user (CLIENT or FREELANCER) can leave a review
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'FREELANCER')")
    public ResponseEntity<ReviewResponseDto> createReview(
            @Valid @RequestBody ReviewRequestDto request) {
        return new ResponseEntity<>(reviewService.createReview(request), HttpStatus.CREATED);
    }

    // Anyone authenticated can view reviews
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
}
