package com.android.memo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.memo.data.Memo
import com.android.memo.data.MemoDao


@Database(entities = [Memo::class],version = 1)
abstract class AppDatabase :RoomDatabase(){

    abstract fun memoDao():MemoDao
    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? =
            instance ?: synchronized(this) {
                instance ?: buildDatabaseInstance(context).also {
                    instance = it
                }
            }

        private fun buildDatabaseInstance(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "memo.db")
                .fallbackToDestructiveMigration()
                .build()
    }
    fun destoryinstance() {
        instance = null
    }
}