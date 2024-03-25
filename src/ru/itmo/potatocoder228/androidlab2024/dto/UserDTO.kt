package dto

import model.User
public data class UserDTO(
    var login: String,
    var password: String
)

public fun UserDTO.toUser() = User(login, password)
public fun User.toUserDTO() = UserDTO(login, password)
