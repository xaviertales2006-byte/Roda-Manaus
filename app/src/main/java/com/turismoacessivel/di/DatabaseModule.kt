package com.turismoacessivel.di

import android.content.Context
import androidx.room.Room
import com.turismoacessivel.data.local.AppDatabase
import com.turismoacessivel.data.local.AvaliacaoDao
import com.turismoacessivel.data.local.FavoritoDao
import com.turismoacessivel.data.local.LocalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "turismo.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideLocalDao(db: AppDatabase): LocalDao = db.localDao()

    @Provides
    fun provideAvaliacaoDao(db: AppDatabase): AvaliacaoDao = db.avaliacaoDao()

    @Provides
    fun provideFavoritoDao(db: AppDatabase): FavoritoDao = db.favoritoDao()
}
