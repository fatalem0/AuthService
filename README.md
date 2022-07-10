# AuthService
an Java authorization service including Spring Security/Hibernate/Postgres

# Endpoints
> Информация о пользователе передается через Authentication header
### Для незарегистрированных пользователей
/validate - проверить доступность имени\
/register - регистрация и аутентификация
### Для зарегистрированных пользователей
/login - только аутентификация\
/animals/create - создать животное\
/animals/{id} (POST) - обновить информацию о животном\
/animals/{id} (DELETE) - удалить животное\
/animals/{id} (GET) - получить информацию о животном по его id\
/animals/my_animals - получить информацию о животных пользователя
