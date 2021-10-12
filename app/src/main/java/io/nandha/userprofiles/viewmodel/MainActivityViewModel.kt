package io.nandha.userprofiles.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.nandha.userprofiles.model.*
import io.nandha.userprofiles.model.data.User
import io.nandha.userprofiles.model.db.CacheDb
import kotlinx.coroutines.flow.Flow

class MainActivityViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: Repository = Repository(Api.create(), CacheDb.getInstance(application.applicationContext))

    fun loadUser(): Flow<PagingData<User>> {
       return repository.getUsers().cachedIn(viewModelScope)
    }
}