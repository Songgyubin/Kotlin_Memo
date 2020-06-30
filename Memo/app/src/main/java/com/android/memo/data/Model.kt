package com.android.memo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "memo")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var content: String,
    var image: String,
    var date: String,
    var isChecked: Boolean = false

):Serializable
