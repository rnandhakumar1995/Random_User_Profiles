package io.nandha.userprofiles.model

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.nandha.userprofiles.model.data.User
import io.nandha.userprofiles.model.db.CacheDb
import kotlinx.coroutines.flow.Flow


class Repository(private val api: Api, private val database: CacheDb) {
    companion object {
        const val NETWORK_COUNT_PER_PAGE = 25
    }

    fun getUsers(): Flow<PagingData<User>> {
        val pagingSourceFactory = { database.cacheDao().getUsers() }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_COUNT_PER_PAGE, enablePlaceholders = false),
            remoteMediator = CacheMediator(api, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}