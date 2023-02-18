package com.example.lanote.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lanote.model.Note
import com.example.lanote.model.NotesDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AddNoteViewModel @Inject constructor ( private val db: NotesDatabase) : ViewModel() {
}