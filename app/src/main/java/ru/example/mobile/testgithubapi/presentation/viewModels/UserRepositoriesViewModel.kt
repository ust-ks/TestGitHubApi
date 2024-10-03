package ru.example.mobile.testgithubapi.presentation.viewModels

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.example.mobile.testgithubapi.data.local.RepoEntity
import ru.example.mobile.testgithubapi.data.local.UserEntity
import ru.example.mobile.testgithubapi.data.remote.RemoteDataSource
import ru.example.mobile.testgithubapi.presentation.utils.toLocalDateTime

class UserRepositoriesViewModel(
    private val remoteDataSource: RemoteDataSource,
    private val userEntity: UserEntity
) : ViewModel() {

    private var _repositories: MutableStateFlow<MutableList<RepoEntity>> =
        MutableStateFlow(mutableListOf())
    val repositories: StateFlow<List<RepoEntity>> get() = _repositories

    fun loadRepositories() {
        viewModelScope.launch(Dispatchers.IO) {
            remoteDataSource.getUserRepositories(userEntity.reposUrl)?.forEach { repoDto ->
                remoteDataSource.getLastCommitFromRepo(repoDto.commitsUrl).also { commit ->
                    val lastCommitDate = commit?.date?.date?.toLocalDateTime() ?: return@forEach
                    val repoEntity = RepoEntity(repoDto, lastCommitDate)
                    _repositories.value = _repositories.value.toMutableList().apply {
                        add(repoEntity)
                    }
                }
            }
        }
    }

}