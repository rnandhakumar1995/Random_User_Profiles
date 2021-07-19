package io.nandha.userprofiles.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.nandha.userprofiles.model.data.ApiUser
import io.nandha.userprofiles.model.data.User

const val STARTING_PAGE_INDEX = 1

class UserPagingSource(private val api: Api) : PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = mapToUser(api.getUser(position).results)
            val nextKey = if (response.isEmpty()) {
                null
            } else {
                position + (params.loadSize / Repository.NETWORK_COUNT_PER_PAGE)
            }
            LoadResult.Page(
                data = response,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            LoadResult.Error(exception)
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
