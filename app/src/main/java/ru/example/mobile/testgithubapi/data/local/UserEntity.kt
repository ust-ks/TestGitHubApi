package ru.example.mobile.testgithubapi.data.local

import ru.example.mobile.testgithubapi.data.remote.models.UserDto

data class UserEntity(
    val id: String,
    val login: String,
    val avatarUrl: String,
    var followersCount: Int?
) {
    constructor(userDto: UserDto): this(
        id = userDto.id,
        login = userDto.login,
        avatarUrl = userDto.avatarUrl,
        followersCount = null
    )
}