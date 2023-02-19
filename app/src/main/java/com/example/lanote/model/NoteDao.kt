package com.example.lanote.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun addNote(contact: Note)

    @Query("select * from Note")
    fun getNote(): Flow<List<Note>>

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun updateNote(note:Note)

}