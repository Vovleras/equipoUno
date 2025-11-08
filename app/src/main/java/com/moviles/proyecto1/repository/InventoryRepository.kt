package com.moviles.proyecto1.repository

import android.content.Context
import android.util.Log
import com.moviles.proyecto1.data.InventoryDB
import com.moviles.proyecto1.data.InventoryDao
import com.moviles.proyecto1.model.Inventory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class InventoryRepository (val context: Context) {
    private var inventoryDao: InventoryDao = InventoryDB.getDatabase(context).inventoryDao()
    suspend fun saveInventory(inventory:Inventory){
        withContext(Dispatchers.IO){
            inventoryDao.saveInventory(inventory)
        }
    }

    suspend fun getListInventory():MutableList<Inventory>{
        return withContext(Dispatchers.IO){
            inventoryDao.getListInventory()
        }
    }

    suspend fun deleteInventory(inventory: Inventory): String = withContext(Dispatchers.IO) {
        inventoryDao.deleteInventory(inventory)
        Log.e( "DELETE INVENTORY", "Inventory ${inventory.name} deleted")
        "${inventory.name} eliminado correctamente"
    }

    suspend fun updateRepository(inventory: Inventory){
        withContext(Dispatchers.IO){
            inventoryDao.updateInventory(inventory)
        }
    }

    suspend fun updateTotalPerProduct(productID: Int, productPrice: Float, productQuantity: Int):Float{
        val total = productQuantity * productPrice
        withContext(Dispatchers.IO) {
            inventoryDao.updateTotal(productID, total)
        }
        return total

    }
}