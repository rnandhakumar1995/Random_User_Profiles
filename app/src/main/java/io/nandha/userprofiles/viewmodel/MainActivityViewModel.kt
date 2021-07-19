package io.nandha.userprofiles.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.nandha.userprofiles.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count

class MainActivityViewModel (application: Application) : AndroidViewModel(application) {
    private var users: Flow<PagingData<User>>? = null
    private val repository: Repository = Repository(Api.create(), CacheDb.getInstance(application.applicationContext))

    fun loadUser(): Flow<PagingData<User>> {
        val newResult = repository.getUsers().cachedIn(viewModelScope)
        users = newResult
        return newResult
    }
}