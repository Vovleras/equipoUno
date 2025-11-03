package com.moviles.proyecto1.repository

import android.content.Context
import com.moviles.proyecto1.data.InventoryDB
import com.moviles.proyecto1.data.InventoryDao

class InventoryRepository (val context: Context) {
    private var inventoryDao: InventoryDao = InventoryDB.getDatabase(context).inventoryDao()
}