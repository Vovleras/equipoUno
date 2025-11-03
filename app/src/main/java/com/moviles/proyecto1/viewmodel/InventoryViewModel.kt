package com.moviles.proyecto1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.moviles.proyecto1.repository.InventoryRepository

class InventoryViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val inventoryRepository = InventoryRepository(context)
}