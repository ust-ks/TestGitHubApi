package ru.example.mobile.testgithubapi.presentation.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.example.mobile.testgithubapi.data.local.RepoEntity
import ru.example.mobile.testgithubapi.data.local.UserEntity
import ru.example.mobile.testgithubapi.presentation.ui.theme.TestGitHubApiTheme
import ru.example.mobile.testgithubapi.presentation.utils.toFormatDateTime
import ru.example.mobile.testgithubapi.presentation.viewModels.UserRepositoriesViewModel

class UserRepositoriesActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = intent.extras?.getSerializable("user") as? UserEntity ?: return
        val viewModel by viewModel<UserRepositoriesViewModel> {
            parametersOf(user)
        }

        viewModel.loadRepositories()

        setContent {
            TestGitHubApiTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    UserInfo(user)
                    ListRepositories(viewModel.repositories)
                }
            }
        }
    }
}

@Composable
private fun UserInfo(user: UserEntity) {
    Row {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Column {
            Text(
                text = user.login,
                modifier = Modifier.padding(5.dp)
            )

            Text(
                text = "Followers: " + user.followersCount,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
private fun ListRepositories(
    resultStateFlow: StateFlow<List<RepoEntity>>
) {
    val result: List<RepoEntity> by resultStateFlow.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(result) {
            RepoItem(it)
        }
    }
}

@Composable
private fun RepoItem(
    repo: RepoEntity
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = repo.name,
            modifier = Modifier.padding(4.dp)
        )

        if (repo.description != null) {
            Text(
                text = repo.description,
                modifier = Modifier.padding(4.dp)
            )
        }

        if (repo.lastCommitDate != null) {
            Text(
                text = "last commit: ${repo.lastCommitDate!!.toFormatDateTime()}",
                modifier = Modifier.padding(4.dp)
            )
        }

        Text(
            text = "default branch: ${repo.defaultBranch}",
            modifier = Modifier.padding(4.dp)
        )

        Text(
            text = "forks = ${repo.forksCount}",
            modifier = Modifier.padding(4.dp)
        )

        Text(
            text = "starts = ${repo.stargazersCount}",
            modifier = Modifier.padding(4.dp)
        )

        if (repo.language != null) {
            Text(
                text = "language: ${repo.language}",
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}