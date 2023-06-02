# java-filmorate
Технологии: Java + Spring Boot + Maven + Lombok + JUnit + RESTful API + JDBC

Данный проект представляет собой бэкенд для сервиса, который работает с фильмами и оценками пользователей и рекомендует фильмы к просмотру.

Основная задача приложения - решить проблему поиска фильмов на вечер. С его помощью вы можете легко найти фильм, который вам понравится. Реализован функционал создания аккаунта, добавления друзей и возможность ставить фильмам лайки. В ленте событий можно увидеть, какие фильмы понравились вашим друзьям. Также есть возможность оставлять отзывы и читать отзывы других пользователей. Поиск поможет найти фильм по ключевым словам. Рекомендации помогут выбрать фильм на основе ваших предпочтений. Функциональность «Популярные фильмы» предлагает вывод самых любимых у зрителей фильмов по жанрам и годам.
## Реализованы следующие эндпоинты:
### 1. Фильмы 
- POST /films - создание фильма

- PUT /films - редактирование фильма

- GET /films - получение списка всех фильмов

- GET /films/{id} - получение информации о фильме по его id

- PUT /films/{id}/like/{userId} — поставить лайк фильму

- DELETE /films/{id}/like/{userId} — удалить лайк фильма

- GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, возвращает первые 10.

### 2. Пользователи
   POST /users - создание пользователя

- PUT /users - редактирование пользователя

- GET /users - получение списка всех пользователей

- GET /users/{id} - получение данных о пользователе по id

- PUT /users/{id}/friends/{friendId} — добавление в друзья

- DELETE /users/{id}/friends/{friendId} — удаление из друзей

- GET /users/{id}/friends — возвращает список друзей

- GET /users/{id}/friends/common/{otherId} — возвращает список друзей, общих с другим пользователем

## Валидация
Данные, которые приходят в запросе на добавление нового фильма или пользователя, проходят проверку по следующим критериям:
### 1. Фильмы
- Название не может быть пустым.

- Максимальная длина описания — 200 символов

- Дата релиза — не раньше 28 декабря 1895 года

- Продолжительность фильма должна быть положительной

### 2. Пользователи
- Электронная почта не может быть пустой и должна быть электронной почтой (аннотация @Email)

- Логин не может быть пустым и содержать пробелы

- Имя для отображения может быть пустым — в таком случае будет использован логин

- Дата рождения не может быть в будущем.

### Схема базы данных

![filmorate_dia](https://github.com/Karnelina/java-filmorate/assets/103586369/c63f9114-44d2-4e1e-aabd-1650f5b7d2fd)

### Примеры запросов:




1. Пользователи


Создание пользователя

 ```sql
 INSERT INTO USERS (NAME, EMAIL, LOGIN, BIRTHDAY)
 VALUES ( ?, ?, ?, ? );
 ```

Редактирование пользователя

 ```sql
 UPDATE USERS
 SET EMAIL = ?,
     LOGIN = ?,
     NAME = ?,
     BIRTHDAY = ?
 WHERE USER_ID = ?
 ```

Получение списка всех пользователей

 ```sql
 SELECT *
 FROM USERS
 ```
Получение информации о пользователе по его id

 ```sql
 SELECT *
 FROM USERS
 WHERE USER_ID = ?
 ```

Добавление в друзья

 ```sql
 INSERT INTO FRIENDS (USER_ID, FRIEND_ID, STATUS)
 VALUES (?, ?, ?)
 ```

Удаление из друзей

 ```sql
 DELETE
 FROM FRIENDS
 WHERE USER_ID = ? AND FRIEND_ID = ?
 ```

Возвращает список пользователей, являющихся его друзьями
 ```sql
 SELECT ut.*
 FROM FRIENDS AS fst
 INNER JOIN USERS AS ut ON ut.USER_ID = fst.FRIEND_ID
 WHERE fst.USER_ID = ?
 ```

Список друзей, общих с другим пользователем

 ```sql
 SELECT ut.*
 FROM USERS AS ut
 INNER JOIN FRIENDS AS fst ON ut.USER_ID = fst.FRIEND_ID
 WHERE ut.USER_ID = ?

 INTERSECT

 SELECT ut.*
 FROM USERS as ut
 INNER JOIN FRIENDS as fst ON ut.USER_ID = fst.FRIEND_ID
 WHERE fst.USER_ID = ?
 ```

Удаляет пользователя
```sql
DELETE 
FROM USERS 
WHERE USER_ID = ?
```

2. Фильмы

Создание фильма

 ```sql
 INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID)
 VALUES (?, ?, ?, ?, ?)
 ```

Редактирование фильма

 ```sql
 UPDATE FILMS
 SET NAME = ?,
     DESCRIPTION = ?,
     RELEASE_DATE = ?,
     DURATION = ?,
     MPA_RATING_ID = ?
 WHERE FILM_ID = ?
 ```

Получение списка всех фильмов

 ```sql
 SELECT ft.*, mpt.NAME, COUNT(flt.USER_ID) AS rate
 FROM FILMS AS ft
 LEFT JOIN MPA_RATING AS mpt ON ft.MPA_RATING_ID = mpt.RATING_ID
 LEFT JOIN FILM_LIKE AS flt ON ft.FILM_ID = flt.FILM_ID
 GROUP BY ft.FILM_ID
 ORDER BY ft.FILM_ID
 ```

Получение информации о фильме по его id

 ```sql
 SELECT ft.*, mpt.NAME, COUNT(flt.USER_ID) AS rate
 FROM FILMS AS ft
 LEFT JOIN MPA_RATING AS mpt ON ft.MPA_RATING_ID = mpt.RATING_ID
 LEFT JOIN FILM_LIKE AS flt ON ft.FILM_ID = flt.FILM_ID
 WHERE ft.FILM_ID = 2
 GROUP BY ft.FILM_ID
 ```

Пользователь ставит лайк фильму

  ```sql
 INSERT INTO FILM_LIKE (FILM_ID, USER_ID)
 VALUES (?, ?)
  ```

Пользователь удаляет лайк

  ```sql
 DELETE
 FROM FILM_LIKE
 WHERE FILM_ID = ? AND USER_ID = ?
  ```

Возвращает список из первых count фильмов по количеству лайков
 ```sql
SELECT ft.*, mpt.NAME, COUNT(flt.USER_ID) AS rate
FROM FILMS AS ft
LEFT JOIN MPA_RATING AS mpt ON ft.MPA_RATING_ID = mpt.RATING_ID
LEFT JOIN FILM_LIKE AS flt ON ft.FILM_ID = flt.FILM_ID
GROUP BY ft.FILM_ID
ORDER BY rate DESC, ft.FILM_ID
LIMIT ?
 ```

Удаляет фильм 
```sql
DELETE 
FROM FILMS 
WHERE FILM_ID = ?
```