package ru.example.mobile.testgithubapi.data.remote.models

import com.google.gson.annotations.SerializedName

data class RepoDto(
    val id: String,
    val name: String,
    val description: String?,
    @SerializedName("commits_url")
    val commitsUrl: String,
    @SerializedName("default_branch")
    val defaultBranch: String,
    @SerializedName("forks_count")
    val forksCount: Int,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    val language: String
)