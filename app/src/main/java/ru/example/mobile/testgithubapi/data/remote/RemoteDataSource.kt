package ru.example.mobile.testgithubapi.data.remote

import retrofit2.await
import ru.example.mobile.testgithubapi.data.remote.models.CommitDto
import ru.example.mobile.testgithubapi.data.remote.models.FollowerDto
import ru.example.mobile.testgithubapi.data.remote.models.RepoDto
import ru.example.mobile.testgithubapi.data.remote.models.UserDto

class RemoteDataSource(
    private val apiService: ApiService
) {

    suspend fun getUsersByQuery(
        query: String,
        page: Int = 1
    ) : List<UserDto>? {
        return try {
            val response = apiService.searchUserByQuery(query, page).await()
            response.items
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getFollowersCountForUser(userDto: UserDto) : Int {
        var page = 1
        var followersCount = 0
        var hasMore = true

        while (hasMore) {
            val followers = getFollowersForUserByPage(userDto.followersUrl, page)

            if (followers.isNullOrEmpty()) {
                hasMore = false
            } else {
                followersCount += followers.size
                page++
            }
        }

        return followersCount
    }

    suspend fun getFollowersForUserByPage(
        url: String,
        page: Int = 1
    ) : List<FollowerDto>? {

        return try {
            val response = apiService.getFollowers(url, page).await()
            response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getUserRepositories(
        url: String,
        page: Int = 1
    ) : List<RepoDto>? {
        return try {
            apiService.getRepositories(url, page).await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getLastCommitFromRepo(
        url: String
    ) : CommitDto? {
        return try {
            val response = apiService.getCommits(url.replace("{/sha}", "")).await()
            response[0].commit
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}