package com.example.lanote.di

import android.app.Application
import androidx.room.Room
import com.example.lanote.model.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): NotesDatabase {
        val database = Room.databaseBuilder(
            app,
            NotesDatabase::class.java,
            "Note_db"
        ).build()
        return database
    }
}