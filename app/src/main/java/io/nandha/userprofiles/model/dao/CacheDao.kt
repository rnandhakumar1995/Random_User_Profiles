package io.nandha.userprofiles.model.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.nandha.userprofiles.model.data.User

@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<User>)

    @Query("SELECT * FROM user WHERE name LIKE :nameQuery ORDER BY email ASC")
    fun searchUser(nameQuery: String): List<User>

    @Query("SELECT * FROM user WHERE email = :email")
    fun getUser(email: String): User

    @Query("SELECT * FROM user")
    fun getUsers(): PagingSource<Int, User>

    @Query("DELETE FROM user")
    suspend fun clearTable()
}