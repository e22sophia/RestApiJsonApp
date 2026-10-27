package com.example.restapijsonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                PostScreen()
            }
        }
    }
}

@Composable
fun PostScreen() {

    var posts by remember {
        mutableStateOf<List<Post>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    val scope = rememberCoroutineScope()

    fun fetchPosts() {

        scope.launch {

            loading = true
            error = null

            try {

                posts = RetrofitClient.api.getPosts()

            } catch (e: Exception) {

                error =
                    "Unable to fetch data. Check your internet connection."

            } finally {

                loading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchPosts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "REST API - JSON Posts",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = {
                fetchPosts()
            },
            enabled = !loading
        ) {
            Text("Refresh Data")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        when {

            loading -> {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }

            error != null -> {

                Column {

                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Button(
                        onClick = {
                            fetchPosts()
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }

            else -> {

                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = posts,
                        key = { it.id }
                    ) { post ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {

                                Text(
                                    text = "Post ${post.id}",
                                    style = MaterialTheme
                                        .typography
                                        .titleMedium
                                )

                                Spacer(
                                    modifier = Modifier
                                        .height(6.dp)
                                )

                                Text(
                                    text = "User ID: ${post.userId}"
                                )

                                Spacer(
                                    modifier = Modifier
                                        .height(6.dp)
                                )

                                Text(
                                    text = post.title
                                )

                                Spacer(
                                    modifier = Modifier
                                        .height(6.dp)
                                )

                                Text(
                                    text = post.body
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}