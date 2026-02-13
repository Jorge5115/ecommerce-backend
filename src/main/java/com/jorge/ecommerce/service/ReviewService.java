package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CreateReviewDTO;
import com.jorge.ecommerce.dto.ReviewDTO;
import com.jorge.ecommerce.entity.Product;
import com.jorge.ecommerce.entity.Review;
import com.jorge.ecommerce.entity.User;
import com.jorge.ecommerce.exception.BadRequestException;
import com.jorge.ecommerce.exception.ResourceNotFoundException;
import com.jorge.ecommerce.repository.OrderRepository;
import com.jorge.ecommerce.repository.ProductRepository;
import com.jorge.ecommerce.repository.ReviewRepository;
import com.jorge.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public ReviewDTO createReview(String userEmail, CreateReviewDTO createReviewDTO) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(createReviewDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (reviewRepository.existsByUserAndProduct(user, product)) {
            throw new BadRequestException("You have already reviewed this product");
        }

        boolean verifiedPurchase = orderRepository.findByUser(user, Pageable.unpaged())
                .getContent()
                .stream()
                .anyMatch(order -> order.getOrderItems()
                        .stream()
                        .anyMatch(item -> item.getProduct().getId().equals(product.getId())));

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(createReviewDTO.getRating());
        review.setComment(createReviewDTO.getComment());
        review.setVerifiedPurchase(verifiedPurchase);

        Review savedReview = reviewRepository.save(review);
        updateProductRating(product);

        return convertToDTO(savedReview);
    }

    @Transactional(readOnly = true)
    public Page<ReviewDTO> getProductReviews(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return reviewRepository.findByProduct(product, pageable).map(this::convertToDTO);
    }

    @Transactional
    public void deleteReview(String userEmail, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUser().getEmail().equals(userEmail)) {
            throw new BadRequestException("You don't have permission to delete this review");
        }

        Product product = review.getProduct();
        reviewRepository.delete(review);
        updateProductRating(product);
    }

    private void updateProductRating(Product product) {
        Double avgRating = reviewRepository.findAverageRatingByProductId(product.getId());
        Integer reviewCount = reviewRepository.findReviewCountByProductId(product.getId());
        product.setAverageRating(avgRating != null ? avgRating : 0.0);
        product.setReviewCount(reviewCount != null ? reviewCount : 0);
        productRepository.save(product);
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setProductId(review.getProduct().getId());
        dto.setProductName(review.getProduct().getName());
        dto.setUserId(review.getUser().getId());
        dto.setUserFirstName(review.getUser().getFirstName());
        dto.setUserLastName(review.getUser().getLastName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setVerifiedPurchase(review.getVerifiedPurchase());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}