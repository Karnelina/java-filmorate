package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewService reviewService;

    @PostMapping
    public Review addNewReview(@RequestBody Review review) {
        log.info("Получен запрос на Добавление нового отзыва.");
        return reviewService.addNewReview(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody Review review) {
        log.info("Получен запрос на Редактирование уже имеющегося отзыва.");
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        log.info("Получен запрос на Удаление уже имеющегося отзыва.");
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Review getReviewById(@PathVariable Long id) {
        log.info("Получен запрос на Получение отзыва по идентификатору.");
        log.info("ID = {}", id);
        return reviewService.getReviewById(id);
    }

    @GetMapping(params = {"filmId"})
    public ResponseEntity<List<Review>> getReviewsByFilmId(@RequestParam(required = false) Long filmId, @RequestParam(defaultValue = "10") Long count) {
        return new ResponseEntity<List<Review>>(reviewService.getReviews(filmId, count), HttpStatus.OK);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Review> getReviews(
            @RequestParam(defaultValue = "10") Long count) {
        log.info("Получен запрос на Получение отзывов ");
        return reviewService.getReviews(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToReview(@PathVariable Long id,
                                @PathVariable Long userId) {
        log.info("Получен запрос на добавление пользователем лайка отзыву.");
        Long rate = 1L;
        reviewService.addRateToReview(id, userId, rate);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislikeToReview(@PathVariable Long id,
                                   @PathVariable Long userId) {
        log.info("Получен запрос на добавление пользователем дизлайка отзыву.");
        Long rate = -1L;
        reviewService.addRateToReview(id, userId, rate);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id,
                           @PathVariable Long userId) {
        log.info("Получен запрос в котором пользователь удаляет лайк/дизлайк отзыву");
        reviewService.deleteRateFromReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeFromReview(@PathVariable Long id,
                                        @PathVariable Long userId) {
        log.info("Получен запрос в котоом пользователь удаляет дизлайк отзыву.");
        reviewService.deleteRateFromReview(id, userId);
    }
}
