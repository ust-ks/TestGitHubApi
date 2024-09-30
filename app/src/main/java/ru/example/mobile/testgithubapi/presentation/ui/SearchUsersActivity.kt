package ru.example.mobile.testgithubapi.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.example.mobile.testgithubapi.data.local.UserEntity
import ru.example.mobile.testgithubapi.presentation.ui.theme.TestGitHubApiTheme
import ru.example.mobile.testgithubapi.presentation.viewModels.SearchUsersViewModel

class SearchUsersActivity : ComponentActivity() {

    private val searchUsersViewModel by viewModel<SearchUsersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TestGitHubApiTheme {
                SearchUsersScreen(searchUsersViewModel)
            }
        }
    }
}

@Composable
fun SearchUsersScreen(
    viewModel: SearchUsersViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(viewModel.searchQuery) { viewModel.searchUsers() }
        ListResults(viewModel.searchResult)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun SearchBar(
    queryStateFlow: MutableStateFlow<String>,
    search: () -> Unit
) {
    val query: String by queryStateFlow.collectAsState()
    Row {
        TextField(
            value = query,
            onValueChange = { queryStateFlow.value = it },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            label = { Text("Введите запрос") }
        )

        Button(
            onClick = { search() }
        ) {
            Text("Поиск")
        }
    }
}

@Composable
private fun ListResults(
    resultStateFlow: StateFlow<List<UserEntity>>
) {
    val result: List<UserEntity> by resultStateFlow.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(result) {
            UserItem(it)
        }
    }
}

@Composable
private fun UserItem(
    user: UserEntity
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Text(
                text = user.login,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Followers: " + user.followersCount,
                modifier = Modifier.padding(16.dp)
            )
        }

    }
}