package com.moviles.proyecto1.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.moviles.proyecto1.model.Inventory
import kotlinx.coroutines.tasks.await
import javax.inject.Inject






class InventoryRepository @Inject constructor(

)  {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("inventory")

    suspend fun saveInventory(inventory: Inventory): String? {
        return try {

            if (inventory.id.isBlank()) {
                throw IllegalArgumentException("El ID del inventario no puede estar vacío")
            }


            val docRef = collection.document(inventory.id)


            docRef.set(inventory).await()


            Log.d("Repository", "Producto creado con ID: ${inventory.id}")
            inventory.id

        } catch (e: Exception) {
            Log.e("Repository", "Error al crear producto: ${e.message}")
            null
        }
    }

    suspend fun getListInventory(): MutableList<Inventory> {
        return try {
            collection.get().await().documents.mapNotNull { doc ->
                val inv = doc.toObject(Inventory::class.java)
                inv?.apply { id = doc.id }
            }.toMutableList()
        } catch (e: Exception) {
            Log.e("Repository", "Error al obtener lista: ${e.message}")
            mutableListOf()
        }
    }

    suspend fun deleteInventory(inventory: Inventory): String {
        return try {
            collection.document(inventory.id).delete().await()
            Log.d("Repository", "Producto eliminado: ${inventory.name}")
            "${inventory.name} eliminado correctamente"
        } catch (e: Exception) {
            Log.e("Repository", "Error al eliminar: ${e.message}")
            "Error al eliminar ${inventory.name}"
        }
    }

    suspend fun updateRepository(inventory: Inventory): Boolean {
        return try {
            collection.document(inventory.id).set(inventory).await()
            Log.d("Repository", "Producto actualizado: ${inventory.id}")
            true
        } catch (e: Exception) {
            Log.e("Repository", "Error al actualizar: ${e.message}")
            false
        }
    }

    suspend fun calculateTotalInventory(): Float {
        return try {
            val inventories = getListInventory()
            inventories.sumOf { (it.total ?: 0f).toDouble() }.toFloat()
        } catch (e: Exception) {
            0f
        }
    }

    suspend fun updateTotalPerProduct(productID: String, productPrice: Float, productQuantity: Int): Float {
        val total = productQuantity * productPrice
        try {
            collection.document(productID).update("total", total).await()
            Log.d("Repository", "Total actualizado: $productID -> $total")
        } catch (e: Exception) {
            Log.e("Repository", "Error al actualizar total: ${e.message}")
        }
        return total
    }
}