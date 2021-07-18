package io.nandha.userprofiles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.nandha.userprofiles.model.Api
import io.nandha.userprofiles.model.Repository
import io.nandha.userprofiles.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count

class MainActivityViewModel : ViewModel() {
    private var users: Flow<PagingData<User>>? = null
    private val repository: Repository = Repository(Api.create())

    fun loadUser(): Flow<PagingData<User>> {
        val newResult = repository.getUsers().cachedIn(viewModelScope)
        users = newResult
        return newResult
    }
}