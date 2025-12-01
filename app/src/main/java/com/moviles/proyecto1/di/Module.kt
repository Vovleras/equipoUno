package com.moviles.proyecto1.di

import android.content.Context
import androidx.room.Room
import com.moviles.proyecto1.data.InventoryDB
import com.moviles.proyecto1.data.InventoryDao
import com.moviles.proyecto1.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideInventoryDB(@ApplicationContext context: Context):InventoryDB{
        return Room.databaseBuilder(
            context,
            InventoryDB::class.java,
            Constants.NAME_DB
        ).build()

    }

    @Singleton
    @Provides
    fun provideDaoReto(inventoryDB:InventoryDB): InventoryDao {
        return inventoryDB.inventoryDao()
    }

}