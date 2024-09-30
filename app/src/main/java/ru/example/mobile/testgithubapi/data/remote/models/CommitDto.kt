package ru.example.mobile.testgithubapi.data.remote.models

import com.google.gson.annotations.SerializedName

data class CommitsResponse(
    val commit: CommitDto
)

data class CommitDto(
    val id: String,
    @SerializedName("author")
    val date: Date
)

data class Date(
    val date: String
)