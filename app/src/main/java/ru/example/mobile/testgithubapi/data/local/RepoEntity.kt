package ru.example.mobile.testgithubapi.data.local

import ru.example.mobile.testgithubapi.data.remote.models.RepoDto
import java.time.LocalDateTime

data class RepoEntity(
    val id: String,
    val name: String,
    val description: String?,
    var lastCommitDate: LocalDateTime?,
    val defaultBranch: String,
    val forksCount: Int,
    val stargazersCount: Int,
    val language: String?
) {
    constructor(repoDto: RepoDto) : this(
        id = repoDto.id,
        name = repoDto.name,
        description = repoDto.description,
        lastCommitDate = null,
        defaultBranch = repoDto.defaultBranch,
        forksCount = repoDto.forksCount,
        stargazersCount = repoDto.stargazersCount,
        language = repoDto.language
    )
}