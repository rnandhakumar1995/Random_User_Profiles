package io.nandha.userprofiles.model

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import io.nandha.userprofiles.model.data.ApiUser
import io.nandha.userprofiles.model.data.User

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
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                println("nandhu Next key -> $nextKey is null ${nextKey == null} AND ${MediatorResult.Success(endOfPaginationReached = remoteKeys != null)}")
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }


        try {
            val apiResponse = api.getUser(page)
            val repos = mapToUser(apiResponse.results)
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
            exception.printStackTrace()
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

    private fun mapToUser(apiUsers: List<ApiUser>): List<User> {
        val result = mutableListOf<User>()
        for (user in apiUsers) {
            result.add(user.run {
                User(
                    email,
                    "${name.title}. ${name.first} ${name.last}",
                    "${location.street}, ${location.city}, ${location.state}, ${location.country} - ${location.postcode}",
                    "${location.coordinates.latitude},${location.coordinates.longitude}",
                    cell, phone, picture.thumbnail, picture.large, dob.date, dob.age
                )
            })
        }
        return result
    }
}