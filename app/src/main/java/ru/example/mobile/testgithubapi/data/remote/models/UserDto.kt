package ru.example.mobile.testgithubapi.data.remote.models

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    val items: List<UserDto>
)

data class UserDto(
    val id: String,
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("followers_url")
    val followersUrl: String
)