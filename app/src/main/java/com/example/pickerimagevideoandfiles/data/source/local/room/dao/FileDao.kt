package com.example.pickerimagevideoandfiles.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.pickerimagevideoandfiles.data.source.local.room.entities.FileData

@Dao
interface FileDao : BaseDao<FileData> {
    @Query("SELECT * FROM file_data WHERE type = 1")
    fun getAllMedia(): List<FileData>

    @Query("SELECT * FROM file_data WHERE type = 0")
    fun getAllFile(): List<FileData>
}