package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {

    List<Review> getReviews(Long count);

    Optional<Review> getReview(Long reviewId);

    Review addReview(Review review);

    Review updateReview(Review review);

    Boolean deleteReview(Long reviewId);

    void addReviewReaction(Long reviewId, Long userId, Long rate);

    void removeReviewReaction(Long reviewId, Long userId);

    List<Review> getAllByFilmId(Long filmId, Long count);
}
