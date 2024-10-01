package ru.example.mobile.testgithubapi.data.local

import ru.example.mobile.testgithubapi.data.remote.models.UserDto
import java.io.Serializable

data class UserEntity(
    val id: String,
    val login: String,
    val avatarUrl: String,
    var reposUrl: String,
    var followersCount: Int?
) : Serializable {
    constructor(userDto: UserDto): this(
        id = userDto.id,
        login = userDto.login,
        avatarUrl = userDto.avatarUrl,
        reposUrl = userDto.reposUrl,
        followersCount = null
    )
}