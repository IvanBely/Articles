# <h1 align="center">Статьи и истории | Article and Stories</h1>

## <h1 align="center">Описание:</h1>
Проект "Статьи и истории" представляет собой Spring-приложение, работающее с локально установленной базой данных MySQL или PostgreSQL. Приложение позволяет пользователям размещать свои статьи на определенный срок (день, неделя, месяц, год или бесконечно). Есть возможность сделать статью публичной, чтобы она была видна на корневой странице, или не публичной, чтобы доступ к ней был только по ссылке и виден на странице автора, авторизованным пользователям.

Пользователи могут оценивать статьи лайками и оставлять комментарии. Есть возможность оценить комментарий лайком для зарегистрированных пользователей. Авторы комментариев могут редактировать и удалять свои комментарии.

Реализована система безопасности с использованием Spring Boot Security. Предусмотрена регистрация и аутентификация по электронной почте. Авторизованные пользователи могут изменить пароль и адрес электронной почты, а также удалить свой аккаунт.

Авторы могут редактировать и удалять свои статьи. Раз в день статьи, у которых истек срок годности, удаляются из базы данных.
Для приятного представления, с помощью библиотеки PrettyTime, видно когда была добавлена статья **(3 часа назад, 1 день назад, 2 недели назад и тд.)**

## Stack:
Java версии 21, Spring Boot версии 3.2.4, Maven, Hibernate, Spring Boot Security, Spring Data JPA, Lombok, MySQL, PostgreSQL, Logback, PrittyTime.

## API Спецификация

* GET /

> Корневая страничка со статьями, которые isPublic, и у которых не истек срок годности.

* GET /{hash}

> Страница определенной статьи, у которой есть username опубликовавшего пользователя, название статьи, описание, кол-во лайков и представлением когда она была опубликована.
> Также список комментариев, с username того кто его написал, кол-вом лайков, текстом и представлением когда он был оставлен. 

* POST /{hash}

> Возможность добавить комментарий к данной статье (зарегестрированному пользователю)

* PUT /{hash}

> Возможность изменить комментарий у данной статьи (пользователю который оставлял данный комментарий)

* DELETE /{hash}

> Возможность удалить комментарий у данной статьи (пользователю который оставлял данный комментарий)

* POST /auth/sing-up

> Отправляем запрос для регестрации позователя, email и username должны быть уникальны

* POST /auth/login

> Отправляем запрос на аутентификацию, в постмане необходимо выбрать поле x-www-form-urlencoded
> (key) -> username (value) -> {имя пользователя}
> (key) -> password (value) -> {пароль пользователя}

* POST /auth/logout

> Завершить ссесию пользователя

* POST /user/{user}

> Отправляем запрос на создание статьи 
