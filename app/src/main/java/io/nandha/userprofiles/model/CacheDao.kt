package io.nandha.userprofiles.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.nandha.userprofiles.model.data.User

@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<User>)

    @Query("SELECT * FROM user WHERE email LIKE :nameQuery ORDER BY email ASC")
    fun searchUser(nameQuery: String): List<User>

    @Query("SELECT * FROM user ORDER BY email ASC LIMIT :startingPosition, 25")
    fun getUsers(startingPosition: Int): List<User>
}