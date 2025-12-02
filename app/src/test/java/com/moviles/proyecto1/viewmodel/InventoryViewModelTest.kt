package com.moviles.proyecto1.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moviles.proyecto1.model.Inventory
import com.moviles.proyecto1.repository.InventoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * Pruebas unitarias para InventoryViewModel
 * Usando JUnit, Mockito y Coroutines Test
 */
@ExperimentalCoroutinesApi
class InventoryViewModelTest {

    // Regla para ejecutar tareas de LiveData de forma síncrona
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Regla de Mockito
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    // Dispatcher para coroutines en pruebas
    private val testDispatcher = UnconfinedTestDispatcher()

    // Mock del repositorio
    @Mock
    private lateinit var inventoryRepository: InventoryRepository

    // ViewModel a probar
    private lateinit var viewModel: InventoryViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = InventoryViewModel(inventoryRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Helper function para usar any() con Kotlin
    private fun <T> anyObject(): T {
        org.mockito.Mockito.any<T>()
        @Suppress("UNCHECKED_CAST")
        return null as T
    }

    /**
     * Test para saveInventory()
     * Verifica que se guarde un producto correctamente y se actualice la lista
     */
    @Test
    fun `saveInventory - guardar y actualizar`() = runTest {
        // Given
        val inventory = Inventory(
            id = "1",
            name = "Producto Test",
            price = 100f,
            quantity = 5,
            total = 500f
        )
        val inventoryList = mutableListOf(inventory)

        `when`(inventoryRepository.saveInventory(inventory)).thenReturn("1")
        `when`(inventoryRepository.getListInventory()).thenReturn(inventoryList)

        // When
        viewModel.saveInventory(inventory)

        // Then
        verify(inventoryRepository).saveInventory(inventory)
        verify(inventoryRepository).getListInventory()
        assert(viewModel.listInventory.value == inventoryList)
        assert(viewModel.progresState.value == false)
    }

    
}

