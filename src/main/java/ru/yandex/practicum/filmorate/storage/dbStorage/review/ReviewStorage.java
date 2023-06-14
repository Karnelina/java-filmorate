package ru.yandex.practicum.filmorate.storage.dbStorage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Optional<Review> addNewReview(Review review);

    List<Review> getAllReviews(Long count);

    Review getReview(Long count);

    List<Review> findAllByFilmId(Long filmId, Long count);

    Optional<Review> getReviewById(Long reviewId);

    Optional<Review> updateReview(Review review);

    void deleteReview(Long reviewId);

    void addReactionToReview(Long reviewId, Long userId, Long reaction);

    void deleteRateFromReview(Long reviewId, Long userId);
}
