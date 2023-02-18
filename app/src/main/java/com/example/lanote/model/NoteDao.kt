package com.example.lanote.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun addNote(contact: Note)

    @Query("select * from Note")
    fun getNote(): Flow<List<Note>>

    @Delete
    suspend fun deleteNote(note: Note)

}