package com.turismoacessivel.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalEntity::class, AvaliacaoEntity::class, FavoritoEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun localDao(): LocalDao
    abstract fun avaliacaoDao(): AvaliacaoDao
    abstract fun favoritoDao(): FavoritoDao
}
