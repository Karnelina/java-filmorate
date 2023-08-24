package ru.yandex.practicum.filmorate.constant;

import java.util.Set;

public class SortingConstant {
    public static final String YEAR_ASCENDING_ORDER = "year";
    public static final String LIKES_ASCENDING_ORDER = "likes";

    public static final Set<String> SORTS = Set.of(YEAR_ASCENDING_ORDER, LIKES_ASCENDING_ORDER);
}
