package io.nandha.userprofiles.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [User::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)

abstract class CacheDb : RoomDatabase() {
    abstract fun reposDao(): CacheDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: CacheDb? = null
        fun getInstance(context: Context): CacheDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,CacheDb::class.java, "cache.db").allowMainThreadQueries().build()
    }
}