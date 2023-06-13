package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewDaoImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Review> getReview(Long reviewId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT R.REVIEW_ID, "
                            + "R.FILM_ID, "
                            + "R.USER_ID, "
                            + "R.CONTENT, "
                            + "R.IS_POSITIVE, "
                            + "SUM(RR.RATE) USEFUL "
                            + "FROM REVIEWS R "
                            + "LEFT JOIN REVIEW_RATES AS RR on R.REVIEW_ID = RR.REVIEW_ID "
                            + "WHERE R.REVIEW_ID = ? "
                            + "GROUP BY R.REVIEW_ID, R.FILM_ID, R.USER_ID, R.CONTENT, R.IS_POSITIVE"
                    , reviewRowMapper()
                    , reviewId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Review> getReviews(Long count) {
        return jdbcTemplate.query("SELECT R.REVIEW_ID, " +
                "R.FILM_ID,  " +
                "R.USER_ID,  " +
                "R.CONTENT,  " +
                "R.IS_POSITIVE,  " +
                "SUM(COALESCE(RR.RATE, 0)) USEFUL " +
                "FROM REVIEWS R  " +
                "LEFT OUTER JOIN REVIEW_RATES RR on R.REVIEW_ID = RR.REVIEW_ID  " +
                "GROUP BY R.REVIEW_ID, R.FILM_ID, R.USER_ID, R.CONTENT, R.IS_POSITIVE  " +
                "ORDER BY USEFUL DESC " +
                "LIMIT ? ", reviewRowMapper(), count);
    }

    @Override
    public Review addReview(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("REVIEWS")
                .usingGeneratedKeyColumns("REVIEW_ID");
        Long reviewId = simpleJdbcInsert
                .executeAndReturnKey(reviewToMap(review)).longValue();
        review.setReviewId(reviewId);
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        jdbcTemplate.update("update REVIEWS "
                        + "set    CONTENT = ?, "
                        + "IS_POSITIVE = ? "
                        + "WHERE REVIEW_ID = ?",
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        return (getReview(review.getReviewId()).orElseThrow(
                () -> new ReviewNotFoundException("")));
    }

    @Override
    public Boolean deleteReview(Long reviewId) {
        return jdbcTemplate.update("DELETE FROM REVIEWS WHERE REVIEW_ID = ?", reviewId) == 1;
    }


    @Override
    public void addReviewReaction(Long reviewId, Long userId, Long rate) {
        removeReviewReaction(reviewId, userId);
        jdbcTemplate.update("INSERT INTO"
                + " REVIEW_RATES (REVIEW_ID, USER_ID, RATE) "
                + "VALUES (?, ?, ?)", reviewId, userId, rate);
    }

    @Override
    public void removeReviewReaction(Long reviewId, Long userId) {
        jdbcTemplate.update("DELETE "
                + "FROM REVIEW_RATES "
                + "WHERE REVIEW_ID = ? and USER_ID = ?", reviewId, userId);
    }

    @Override
    public List<Review> getAllByFilmId(Long filmId, Long count) {
        return jdbcTemplate.query("SELECT R.REVIEW_ID, "
                + "R.FILM_ID, "
                + "R.USER_ID, "
                + "R.CONTENT, "
                + "R.IS_POSITIVE, "
                + "COALESCE(SUM(RR.RATE), 0) USEFUL "
                + "FROM REVIEWS R "
                + "LEFT OUTER JOIN REVIEW_Rates RR on R.REVIEW_ID = RR.REVIEW_ID "
                + "WHERE FILM_ID = ? "
                + "group by R.REVIEW_ID, R.FILM_ID, R.USER_ID, R.CONTENT, R.IS_POSITIVE "
                + "ORDER BY USEFUL desc "
                + "LIMIT ? ", reviewRowMapper(), filmId, count);
    }

    private RowMapper<Review> reviewRowMapper() {
        return (rs, rowNum) -> new Review(
                rs.getLong("REVIEW_ID"),
                rs.getString("CONTENT"),
                rs.getBoolean("IS_POSITIVE"),
                rs.getLong("USER_ID"),
                rs.getLong("FILM_ID"),
                rs.getLong("USEFUL"));
    }

    private Map<String, Object> reviewToMap(Review review) {
        Map<String, Object> values = new HashMap<>();
        values.put("REVIEW_ID", review.getReviewId());
        values.put("CONTENT", review.getContent());
        values.put("IS_POSITIVE", review.getIsPositive());
        values.put("USER_ID", review.getUserId());
        values.put("FILM_ID", review.getFilmId());
        values.put("USEFUL", review.getUseful());
        return values;
    }
}
