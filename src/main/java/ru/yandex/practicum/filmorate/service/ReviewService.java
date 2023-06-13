package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dbStorage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public Review addNewReview(Review review) {
        filmStorage.getFilmById(review.getFilmId()).orElseThrow(
                () -> new FilmNotFoundException(String.format(
                        "Фильм с ID %s не найден", review.getFilmId())));
        userStorage.getUserById(review.getUserId()).orElseThrow(
                () -> new UserNotFoundException(String.format(
                        "Пользователь с ID %s не найден", review.getUserId())));
        return reviewStorage.addNewReview(review).orElseThrow(
                () -> new ReviewAlreadyExistsException(String.format(
                        "Отзыв с ID %s уже существует", review.getReviewId())));
    }

    public Review updateReview(Review review) {
        reviewStorage.getReviewById(review.getReviewId()).orElseThrow(() -> new ReviewNotFoundException(String.format(
                "Отзыв с ID %s не найден", review.getReviewId())));
        return reviewStorage.updateReview(review).get();
    }

    public void deleteReview(Long reviewId) {
        reviewStorage.deleteReview(reviewId);
    }

    public Review getReviewById(Long id) {
        return reviewStorage.getReviewById(id).orElseThrow(
                () -> new ReviewNotFoundException(String.format(
                        "Отзыв с ID %s не найден", id)));
    }

    public List<Review> getReviews(Long filmId, Long count) {
        if (filmId == null) {
            return reviewStorage.getAllReviews(count);
        } else {
            filmStorage.getFilmById(filmId).orElseThrow(
                    () -> new FilmNotFoundException(String.format(
                            "Фильм с ID %s не найден", filmId)));
            return reviewStorage.findAllByFilmId(filmId,count);
        }
    }

    public List<Review> getReviews(Long count) {

        return Optional.ofNullable(reviewStorage.getAllReviews(count)).orElse(new ArrayList<>());

    }

    public void addRateToReview(Long reviewId, Long userId, Long rate) {
        userStorage.getUserById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format(
                        "Пользователь с ID %s не найден", userId)));
        reviewStorage.getReviewById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException(String.format(
                        "Ревью с ID %s не найдено", reviewId)));
        reviewStorage.addReactionToReview(reviewId, userId, rate);
    }

    public void deleteRateFromReview(Long reviewId, Long userId) {
        userStorage.getUserById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format(
                        "Пользователь с ID %s не найден", userId)));
        reviewStorage.getReviewById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException(String.format(
                        "Отзыв с ID %s не найден", reviewId)));
        reviewStorage.deleteRateFromReview(reviewId, userId);
    }
}
