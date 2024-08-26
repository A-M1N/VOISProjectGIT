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
    private var currentPage = 1
    private var totalItems = 0
    private var query = ""

    suspend fun searchUsers(query: String) {
        this.query = query
        currentPage = 1
        val result = apiService.searchUsers(query, currentPage)
        totalItems = result.total_count
        _users.value = result.items
    }

    suspend fun loadMoreUsers() {
        if (_users.value.size < totalItems) {
            currentPage++
            val result = apiService.searchUsers(query, currentPage)
            _users.value = _users.value + result.items
        }
    }
}

