package ru.yandex.practicum.filmorate.storage.dbStorage.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    private final ReviewDao reviewDao;

    @Override
    public Optional<Review> addNewReview(Review review) {
        return Optional.ofNullable(reviewDao.addReview(review));
    }

    @Override
    public List<Review> getAllReviews(Long count) {
        List<Review> result = Optional.of(reviewDao.getReviews(count))
                .orElse(new ArrayList<>());
        if (!result.isEmpty()) return result;
        return new ArrayList<>();
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewDao.getReview(reviewId).orElseThrow(
                () -> new ReviewNotFoundException(""));
    }

    @Override
    public List<Review> findAllByFilmId(Long filmId, Long count) {
        return reviewDao.getAllByFilmId(filmId, count);
    }

    @Override
    public Optional<Review> getReviewById(Long reviewId) {
        return Optional.ofNullable(reviewDao.getReview(reviewId)).orElseThrow(
                () -> new ReviewNotFoundException(""));
    }

    @Override
    public Optional<Review> updateReview(Review review) {

        return Optional.ofNullable(reviewDao.updateReview(review));
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewDao.deleteReview(reviewId);
    }

    @Override
    public void addReactionToReview(Long reviewId, Long userId, Long rate) {
        reviewDao.addReviewReaction(reviewId, userId, rate);
    }

    @Override
    public void deleteRateFromReview(Long reviewId, Long userId) {
        reviewDao.removeReviewReaction(reviewId, userId);
    }
}
