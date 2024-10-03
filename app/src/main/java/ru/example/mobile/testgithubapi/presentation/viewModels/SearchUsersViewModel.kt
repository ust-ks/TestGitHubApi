package ru.example.mobile.testgithubapi.presentation.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.example.mobile.testgithubapi.data.local.UserEntity
import ru.example.mobile.testgithubapi.data.remote.RemoteDataSource
import ru.example.mobile.testgithubapi.data.remote.models.UserDto

class SearchUsersViewModel(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {

    var searchQueryField: MutableStateFlow<String> = MutableStateFlow("")

    private var _searchResult = mutableStateListOf<UserEntity>()
    val searchResult: SnapshotStateList<UserEntity> get() = _searchResult

    private var loadUsersJob: Job? = null
    private var loadFollowersUserJob: MutableList<Job> = mutableListOf()

    private var currentQuery = ""
    private var nextPage = 1
    private var _isAllUsersLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAllUsersLoaded: StateFlow<Boolean> = _isAllUsersLoaded

    fun searchUsers() {
        if (searchQueryField.value.isBlank()) return

        loadUsersJob?.cancel()
        loadFollowersUserJob.map { it.cancel() }

        _searchResult.clear()
        currentQuery = searchQueryField.value
        nextPage = 1
        _isAllUsersLoaded.value = false

        loadUsers()
    }

    fun loadUsers() {
        if (loadUsersJob?.isActive == true) return
        if (_isAllUsersLoaded.value) return
        loadUsersJob = viewModelScope.launch(Dispatchers.IO) {
            val userDtos = remoteDataSource.getUsersByQuery(currentQuery, nextPage)
            if (userDtos.isNullOrEmpty()) {
                _isAllUsersLoaded.value = true
                return@launch
            }

            _searchResult.addAll(userDtos.map { UserEntity(it) }.toMutableList())
            nextPage += 1

            userDtos.forEach { loadFollowers(it) }
        }
    }

    private suspend fun loadFollowers(userDto: UserDto) {
        val job = viewModelScope.launch {
            remoteDataSource.getFollowersCountForUser(userDto).also { followersCount ->
                try {
                    val index = _searchResult.indexOf(UserEntity(userDto))
                    _searchResult[index] = _searchResult[index].copy(followersCount = followersCount)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        loadFollowersUserJob.add(job)
    }

}