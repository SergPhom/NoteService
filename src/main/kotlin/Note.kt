package ru.netology

data class Note(
    val id: Int = 0,
    val ownerId: Int = 0,
    val needWiki: Boolean = false,
    val title: String= "",
    val text: String = "",
    val date: Int = 0,
    val readComments: Int = 0,
    val viewUrl: String = "",
    val privacyView: Array<String> = arrayOf("all"),
    val privacyComment: Array<String> = arrayOf("all"),
    val canComment: Boolean = false,
    val isDeleted: Boolean = false
)
//all – доступно всем пользователям;
// friends – доступно друзьям текущего пользователя;
// friends_of_friends / friends_of_friends_only – доступно друзьям и друзьям друзей
// / друзьям друзей текущего пользователя (friends_of_friends_only появился с версии 5.32);
// nobody / only_me – недоступно никому / доступно только мне;
// list{list_id} – доступно друзьям текущего пользователя из списка с идентификатором {list_id};
// {user_id} – доступно другу с идентификатором {user_id};
// -list{list_id} – недоступно друзьям текущего пользователя из списка с идентификатором {list_id};
// -{user_id} – недоступно другу с идентификатором {user_id}.