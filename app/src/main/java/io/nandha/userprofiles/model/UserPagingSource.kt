package io.nandha.userprofiles.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

private const val STARTING_PAGE_INDEX = 1

class UserPagingSource(private val api: Api): PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = api.getUser(position)
            val nextKey = if (response.results.isEmpty()) {
                null
            } else {
                position + (params.loadSize / Repository.NETWORK_COUNT_PER_PAGE)
            }
            LoadResult.Page(
                data = response.results,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            LoadResult.Error(exception)
        }
    }

}
