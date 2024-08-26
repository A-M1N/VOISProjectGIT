package com.example.voisgit

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class UserRepository {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val apiService: GitHubApiService = GitHubApiService.create()

    suspend fun searchUsers(query: String) {
        val result = apiService.searchUsers(query)
        _users.value = result.items
    }


    suspend fun loadMoreUsers(query: String, page: Int) {

        val result = apiService.searchUsers("$query&page=$page")
        _users.value = _users.value + result.items
    }
}

