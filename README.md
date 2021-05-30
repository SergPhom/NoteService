# Проект Сервиса Note

## Реализованы:
* data class Comment и data class Note
* interface CRUDservice 
* синглтоны NoteService и CommentService реализующие CRUDservice

## В синглтонах реализованы методы:
* clear() для очистки данных синглтона перед тестом
* getNotes и getComments для доступа к приватным полям
* CRUD методы для дата классов с выбросом исключений

## Так же реализованы 36 автотестов для проверки методов

### поле guid в методе CommentService.add() пропущено по причине неясного назначения