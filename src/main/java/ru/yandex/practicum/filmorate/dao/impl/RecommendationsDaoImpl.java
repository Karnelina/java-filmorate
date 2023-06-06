package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class RecommendationsDaoImpl implements RecommendationsDao {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDaoImpl filmDao;

    public RecommendationsDaoImpl(JdbcTemplate jdbcTemplate, FilmDaoImpl filmDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDao = filmDao;
    }

    private List<Integer> getRecommendationsIds(long id) {
        String getUsersAndLikes = "SELECT USER_ID, GROUP_CONCAT(DISTINCT FILM_ID ORDER BY FILM_ID ASC SEPARATOR ',') AS FILMS " +
                "FROM FILM_LIKE " +
                "GROUP BY USER_ID";
        HashMap<Long, List<Integer>> actualUsersAndFilms = jdbcTemplate.query(getUsersAndLikes, rs -> {
            HashMap<Long, List<Integer>> usersAndFilms = new HashMap<>();
            while (rs.next()) {
                List<Integer> filmIds = new ArrayList<>();
                String films = rs.getString("FILMS");
                if (films != null && !films.isBlank()) {
                    String[] splitFilms = films.split(",");
                    Integer[] result = Arrays.stream(splitFilms).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
                    filmIds = new ArrayList<>(Arrays.asList(result));
                }
                usersAndFilms.put(rs.getLong("USER_ID"), filmIds);
            }
            return usersAndFilms;
        });
        List<Integer> userFavoriteFilms = new ArrayList<>();
        long userIdWithMaxCoincidences = 0;
        if (actualUsersAndFilms.get(id) != null && !actualUsersAndFilms.get(id).isEmpty()) {
            userFavoriteFilms = actualUsersAndFilms.get(id);
            actualUsersAndFilms.remove(id);
            HashMap<Long, Integer> coincidences = new HashMap<>();
            for (Map.Entry<Long, List<Integer>> entry : actualUsersAndFilms.entrySet()) {
                List<Integer> actualFilms = new ArrayList<>(entry.getValue());
                actualFilms.retainAll(userFavoriteFilms);
                coincidences.put(entry.getKey(), actualFilms.size());
            }
            userIdWithMaxCoincidences = Collections.max(coincidences.entrySet(), Map.Entry.comparingByValue()).getKey();
        }
        List<Integer> recommendations = actualUsersAndFilms.get(userIdWithMaxCoincidences);
        recommendations.removeAll(userFavoriteFilms);
        return recommendations;
    }

    @Override
    public Collection<Film> getUserRecommendations(long id) {
        List<Integer> recommendationsIds = getRecommendationsIds(id);
        List<Film> recommendations = filmDao.getFilmsInIds(recommendationsIds);
        return recommendations;
    }
}