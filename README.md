# AuthService
an Java authorization service including Spring Security/Hibernate/Postgres

# Endpoints
> Информация о пользователе передается через Authentication header

/validate - проверить доступность имени\
/register - регистрация и аутентификация\
/login - только аутентификация
### Для аутентифицированных пользователей
/animals/create - создать животное\
/animals/{id} (POST) - обновить информацию о животном\
/animals/{id} (DELETE) - удалить животное\
/animals/{id} (GET) - получить информацию о животном по его id\
/animals/my_animals - получить информацию о животных пользователя
