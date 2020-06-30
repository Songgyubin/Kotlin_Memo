package com.android.memo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MemoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemo(memo: Memo)

    @Query("DELETE FROM memo WHERE id = :id")
    fun deleteMemoById(id:Int)

    @Query("SELECT * FROM memo ORDER BY date DESC")
    fun getAllMemos():LiveData<List<Memo>>
}