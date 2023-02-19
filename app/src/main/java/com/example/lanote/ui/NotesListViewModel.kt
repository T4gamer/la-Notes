package com.example.lanote.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lanote.model.Note
import com.example.lanote.model.NotesDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotesListViewModel @Inject constructor (
    private val db: NotesDatabase
): ViewModel() {

    var Notes by mutableStateOf(emptyList<Note>())
        private set

    init {
        getNotes()
    }

    fun getNotes(){
        db.dao.getNote().onEach { notesList ->
            Notes = notesList
        }.launchIn(viewModelScope)
    }

    fun deleteNote(note:Note){
        viewModelScope.launch {
            db.dao.deleteNote(note)
        }
    }

    fun addNote(title:String , content:String) {
        viewModelScope.launch {
            val note = Note(Title = title, Content = content)
            db.dao.addNote(note)
        }
    }

    fun updateNote(note:Note){
        viewModelScope.launch {
            db.dao.updateNote(note)
        }
    }

}