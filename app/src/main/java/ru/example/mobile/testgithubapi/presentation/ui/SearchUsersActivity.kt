package ru.example.mobile.testgithubapi.presentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.example.mobile.testgithubapi.data.local.UserEntity
import ru.example.mobile.testgithubapi.presentation.ui.theme.TestGitHubApiTheme
import ru.example.mobile.testgithubapi.presentation.viewModels.SearchUsersViewModel

class SearchUsersActivity : ComponentActivity() {

    private val viewModel by viewModel<SearchUsersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TestGitHubApiTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    SearchBar(viewModel.searchQueryField) { viewModel.searchUsers() }
                    ListResults(
                        result = viewModel.searchResult,
                        isAllUsersLoaded = viewModel.isAllUsersLoaded,
                        onItemClick = { startUserRepositoriesActivity(it) },
                        loadMore = { viewModel.loadUsers() }
                    )
                }
            }
        }
    }

    private fun startUserRepositoriesActivity(user: UserEntity) {
        val intent = Intent(this, UserRepositoriesActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition", "ResourceType")
@Composable
private fun SearchBar(
    queryStateFlow: MutableStateFlow<String>,
    search: () -> Unit
) {
    val query: String by queryStateFlow.collectAsState()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { queryStateFlow.value = it },
            modifier = Modifier.weight(1f),
            label = { Text("User") }
        )

        Spacer(modifier = Modifier.width(5.dp))

        Button(
            onClick = { search() },
            shape = CircleShape,
            modifier = Modifier
                .size(50.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                Icons.Rounded.Search,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
        }
    }
}

@Composable
private fun ListResults(
    result: SnapshotStateList<UserEntity>,
    isAllUsersLoaded: StateFlow<Boolean>,
    onItemClick: (UserEntity) -> Unit,
    loadMore: () -> Unit
) {
    val isAll: Boolean by isAllUsersLoaded.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(result) {
                UserItem(it) { onItemClick(it) }
            }
            if (!isAll && result.isNotEmpty()) {
                item {
                    Button(
                        onClick = { loadMore() }
                    ) {
                        Text(text = "load more")
                    }
                }
            }
        }
    }
}

@Composable
private fun UserItem(
    user: UserEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
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

            Column {
                Text(
                    text = user.login,
                    modifier = Modifier.padding(4.dp)
                )

                Text(
                    text = user.followersCount?.let { "Followers: $it" } ?: "Loading...",
                    modifier = Modifier.padding(4.dp)
                )
            }

        }

    }
}