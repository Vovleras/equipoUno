package com.moviles.proyecto1.viewmodel

import android.app.Application
import android.util.Log
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
    private val _total = MutableLiveData<Float>()

     val total: LiveData<Float> get() = _total

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    private val _totalPerProduct = MutableLiveData<Float>()
    val totalPerProduct: LiveData<Float> get() = _totalPerProduct

    private val _deleteMessage = MutableLiveData<String>()
    val deleteMessage: LiveData<String> get() = _deleteMessage

    private val _saveMessage = MutableLiveData<String>()
    val saveMessage: LiveData<String> get() = _saveMessage


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
            Log.d("AddProduct", "Producto guardado: $inventory")
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


    fun calculateTotalInventory(){
        val inventary = _listInventory.value ?: mutableListOf()
        var total = 00.0f
        for (item in inventary) {
            total += item.total?:0.0f
        }
        _total.postValue (total)
    }

    fun calculateTotalPerProduct(productID: Int, price: Float, quantity: Int) {
        viewModelScope.launch {
            try {
                val total = inventoryRepository.updateTotalPerProduct(productID, price, quantity)
                _totalPerProduct.postValue(total)

            }catch (e: Exception   ){

                _totalPerProduct.postValue(0.0f)
            }

        }
    }

    fun deleteProduct(inventory: Inventory)  {
        viewModelScope.launch {
            _progresState.value = true
            try {
                 val msg = inventoryRepository.deleteInventory(inventory)
                _deleteMessage.postValue(msg)
                Log.e("DELETE VM", msg)
                _progresState.value = false
            } catch (e: Exception) {
                _deleteMessage.postValue("Error al eliminar")
                _progresState.value = false
            }


        }
    }

    fun totalProduct(precio: Float, cantidad: Int): Float {
        val total = precio * cantidad
        return total
    }

}