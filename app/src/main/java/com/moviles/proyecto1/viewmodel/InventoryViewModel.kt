package com.moviles.proyecto1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.moviles.proyecto1.repository.InventoryRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.moviles.proyecto1.model.Inventory

class InventoryViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val inventoryRepository = InventoryRepository(context)

    private val _listInventory = MutableLiveData<MutableList<Inventory>>()
    val listInventory: LiveData<MutableList<Inventory>> get() = _listInventory

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState


    fun saveInventory(inventory: Inventory) {
        viewModelScope.launch {

            _progresState.value = true
            try {
                inventoryRepository.saveInventory(inventory)
                // En caso de que no se sincronice se agrega getListInventory() (igual en delete)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun getListInventory() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listInventory.value = inventoryRepository.getListInventory()
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }

        }
    }

    fun deleteInventory(inventory: Inventory) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                inventoryRepository.deleteInventory(inventory)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }

        }
    }

    fun updateInventory(inventory: Inventory) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                inventoryRepository.updateRepository(inventory)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun totalProducto(price: Int, quantity: Int): Double {
        val total = price * quantity
        return total.toDouble()
    }

    fun calcularTotalInventario(): Double {
        val inventary = _listInventory.value ?: mutableListOf()
        var total = 0.0
        for (item in inventary) {
            total += item.price * item.quantity
        }
        return total
    }
}