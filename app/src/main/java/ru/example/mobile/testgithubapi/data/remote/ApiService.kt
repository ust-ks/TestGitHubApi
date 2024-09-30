package ru.example.mobile.testgithubapi.data.remote

import okhttp3.ResponseBody
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
    fun searchUserByQuery(@Query("q") query: String): Call<UsersResponse>

    @GET
    fun getFollowers(@Url url: String): Call<List<FollowerDto>>

    @GET
    fun getRepositories(@Url url: String): Call<List<RepoDto>>

    @GET
    fun getCommits(@Url url: String): Call<List<CommitsResponse>>
}