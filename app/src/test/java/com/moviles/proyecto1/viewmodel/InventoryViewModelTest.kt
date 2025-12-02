package com.moviles.proyecto1.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moviles.proyecto1.repository.InventoryRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock

class InventoryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var inventoryViewModel: InventoryViewModel

    @Mock
    lateinit var inventoryRepository: InventoryRepository

    @Before
    fun setUp() {
        TODO("Not yet implemented")
    }



}