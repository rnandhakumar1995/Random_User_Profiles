package io.nandha.userprofiles.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow


class Repository(private val api: Api) {
    companion object{
        const val NETWORK_COUNT_PER_PAGE = 10
    }

    fun getUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_COUNT_PER_PAGE,
                enablePlaceholders = false
            ), pagingSourceFactory = { UserPagingSource(api) }).flow
    }
}