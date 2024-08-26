package com.example.voisgit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubUserSearchApp()
        }
    }
}

@Composable
fun GitHubUserSearchApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "search") {
        composable("search") { SearchScreen(navController) }
        composable("results/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            ResultsScreen(navController, query)
        }
    }
}

@Composable
fun SearchScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search GitHub Users") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (query.isNotBlank()) {
                    navController.navigate("results/$query")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
    }
}

@Composable
fun ResultsScreen(navController: NavController, query: String) {
    val viewModel = remember { UserRepository() }
    val users by viewModel.users.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(query) {
        viewModel.searchUsers(query)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(state = listState) {
            items(users) { user ->
                UserItem(user)
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Row(modifier = Modifier.padding(8.dp)) {
        Image(
            painter = rememberImagePainter(data = user.avatar_url),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = user.login)
            Text(text = "ID: ${user.id}")
        }
    }
}
