package com.moviles.proyecto1.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.moviles.proyecto1.model.Inventory
import androidx.room.Room
import com.moviles.proyecto1.utils.Constants.NAME_DB

@Database (entities = [Inventory::class], version = 1)
abstract class InventoryDB: RoomDatabase() {
    abstract fun inventoryDao(): InventoryDao
}