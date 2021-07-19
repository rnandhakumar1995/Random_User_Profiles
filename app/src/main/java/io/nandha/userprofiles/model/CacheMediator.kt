package io.nandha.userprofiles.model

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction

@OptIn(ExperimentalPagingApi::class)
class CacheMediator(
    private val api: Api,
    private val cacheDb: CacheDb
) : RemoteMediator<Int, User>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val apiResponse = api.getUser(page)
            val repos = apiResponse.results
            val endOfPaginationReached = repos.isEmpty()
            cacheDb.withTransaction {
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = repos.map {
                    RemoteKeys(repoId = it.email, prevKey = prevKey, nextKey = nextKey)
                }
                cacheDb.remoteKeysDao().insertAll(keys)
                cacheDb.reposDao().insertAll(repos)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, User>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                cacheDb.remoteKeysDao().remoteKeysRepoId(repo.email)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, User>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                cacheDb.remoteKeysDao().remoteKeysRepoId(repo.email)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, User>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.email?.let { repoId ->
                cacheDb.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }
}