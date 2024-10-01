package ru.example.mobile.testgithubapi.presentation.viewModels

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.example.mobile.testgithubapi.data.local.RepoEntity
import ru.example.mobile.testgithubapi.data.local.UserEntity
import ru.example.mobile.testgithubapi.data.remote.ApiService
import ru.example.mobile.testgithubapi.data.remote.models.CommitsResponse
import ru.example.mobile.testgithubapi.data.remote.models.RepoDto
import ru.example.mobile.testgithubapi.presentation.utils.toLocalDateTime
import java.time.LocalDateTime

class UserRepositoriesViewModel(
    private val apiService: ApiService,
    private val userEntity: UserEntity
) : ViewModel() {

    private var _repositories: MutableStateFlow<MutableList<RepoEntity>> =
        MutableStateFlow(mutableListOf())
    val repositories: StateFlow<List<RepoEntity>> get() = _repositories

    fun loadRepositories() {
        apiService.getRepositories(userEntity.reposUrl).enqueue(
            object : Callback<List<RepoDto>> {
                override fun onResponse(call: Call<List<RepoDto>>, response: Response<List<RepoDto>>) {
                    val repos = response.body() ?: return
                    repos.forEach { repoDto ->
                        val repoEntity = RepoEntity(repoDto)
                        _repositories.value = _repositories.value.toMutableList().apply { add(repoEntity) }
                        getDateLastCommit(repoDto.commitsUrl) { date ->
                            val index = _repositories.value.indexOf(repoEntity)
                            _repositories.value = _repositories.value.toMutableList().apply {
                                this[index].lastCommitDate = date
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<RepoDto>>, t: Throwable) {
                    t.printStackTrace()
                    // todo handle error
                }
            }
        )
    }

    private fun getDateLastCommit(url: String, updateDateLastCommit: (LocalDateTime) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getCommits(url.replace("{/sha}", "")).enqueue(
                object : Callback<List<CommitsResponse>> {
                    override fun onResponse(call: Call<List<CommitsResponse>>, response: Response<List<CommitsResponse>>) {
                        val commits = response.body() ?: return
                        val date = commits[0].commit.date.date.toLocalDateTime() ?: return
                        updateDateLastCommit(date)
                    }

                    override fun onFailure(call: Call<List<CommitsResponse>>, t: Throwable) {
                        t.printStackTrace()
                        // todo handle error
                    }
                }
            )
        }
    }

}