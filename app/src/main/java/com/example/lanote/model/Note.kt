package com.example.lanote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var Title: String,
    var Content: String
) : Parcelable
