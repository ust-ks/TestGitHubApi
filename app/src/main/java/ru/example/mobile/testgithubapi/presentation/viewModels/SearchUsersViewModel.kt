package ru.example.mobile.testgithubapi.presentation.viewModels

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.example.mobile.testgithubapi.data.local.UserEntity
import ru.example.mobile.testgithubapi.data.remote.models.UsersResponse
import ru.example.mobile.testgithubapi.data.remote.ApiService
import ru.example.mobile.testgithubapi.data.remote.models.FollowerDto

class SearchUsersViewModel(
    private val apiService: ApiService
) : ViewModel() {

    var searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    private var _searchResult: MutableStateFlow<MutableList<UserEntity>> =
        MutableStateFlow(mutableListOf())
    val searchResult: StateFlow<List<UserEntity>> get() = _searchResult

    fun searchUsers() {
        val query: String = searchQuery.value
        if (query.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            apiService.searchUserByQuery(query).enqueue(
                object : Callback<UsersResponse> {
                    override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                        val users = response.body()?.items ?: return
                        users.forEach { userDto ->
                            val userEntity = UserEntity(userDto)
                            _searchResult.value = _searchResult.value.toMutableList().apply { add(userEntity) }
                            getCountFollowers(userDto.followersUrl) { count ->
                                val index = _searchResult.value.indexOf(userEntity)
                                _searchResult.value[index].followersCount = count
                            }
                        }
                    }

                    override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                        t.printStackTrace()
                        // todo handle error
                    }
                }
            )
        }
    }

    private fun getCountFollowers(url: String, updateFollowersCount: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getFollowers(url).enqueue(
                object : Callback<List<FollowerDto>> {
                    override fun onResponse(call: Call<List<FollowerDto>>, response: Response<List<FollowerDto>>) {
                        val followers = response.body() ?: return
                        updateFollowersCount(followers.size)
                    }

                    override fun onFailure(call: Call<List<FollowerDto>>, t: Throwable) {
                        t.printStackTrace()
                        // todo handle error
                    }
                }
            )
        }
    }

}