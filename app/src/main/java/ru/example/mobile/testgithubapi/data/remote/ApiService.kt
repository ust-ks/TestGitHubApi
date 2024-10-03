package ru.example.mobile.testgithubapi.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import ru.example.mobile.testgithubapi.data.remote.models.CommitsResponse
import ru.example.mobile.testgithubapi.data.remote.models.FollowerDto
import ru.example.mobile.testgithubapi.data.remote.models.RepoDto
import ru.example.mobile.testgithubapi.data.remote.models.UsersResponse

interface ApiService {

    @GET("/search/users")
    fun searchUserByQuery(
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): Call<UsersResponse>

    @GET
    fun getFollowers(
        @Url url: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100
    ): Call<List<FollowerDto>>

    @GET
    fun getRepositories(
        @Url url: String,
        @Query("page") page: Int = 1
    ): Call<List<RepoDto>>

    @GET
    fun getCommits(@Url url: String): Call<List<CommitsResponse>>
}