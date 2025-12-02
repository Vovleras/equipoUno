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

    /**
     * Test para saveInventory() cuando ocurre una excepción
     */
    @Test
    fun `saveInventory - excepcion`() = runTest {
        // Given
        val inventory = Inventory(id = "1", name = "Test", price = 100f, quantity = 1)

        `when`(inventoryRepository.saveInventory(inventory)).thenThrow(RuntimeException("Error"))

        // When
        viewModel.saveInventory(inventory)

        // Then
        assert(viewModel.progresState.value == false)
    }

    /**
     * Test para getListInventory()
     * Verifica que se obtenga la lista de inventario correctamente
     */
    @Test
    fun `getListInventory - obtener lista`() = runTest {
        // Given
        val inventoryList = mutableListOf(
            Inventory(id = "1", name = "Producto 1", price = 100f, quantity = 5, total = 500f),
            Inventory(id = "2", name = "Producto 2", price = 200f, quantity = 3, total = 600f),
            Inventory(id = "3", name = "Producto 3", price = 300f, quantity = 9, total = 1200f)
        )

        `when`(inventoryRepository.getListInventory()).thenReturn(inventoryList)

        // When
        viewModel.getListInventory()

        // Then
        verify(inventoryRepository).getListInventory()
        assert(viewModel.listInventory.value == inventoryList)
        assert(viewModel.progresState.value == false)
    }

    /**
     * Test para getListInventory() cuando ocurre una excepción
     */
    @Test
    fun `getListInventory - excepcion`() = runTest {
        // Given
        `when`(inventoryRepository.getListInventory()).thenThrow(RuntimeException("Error"))

        // When
        viewModel.getListInventory()

        // Then
        assert(viewModel.progresState.value == false)
    }

    /**
     * Test para updateInventory()
     * Verifica que se actualice un producto correctamente
     */
    @Test
    fun `updateInventory - actualizar`() = runTest {
        // Given
        val inventory = Inventory(
            id = "1",
            name = "Producto Actualizado",
            price = 150f,
            quantity = 10,
            total = 1500f
        )

        `when`(inventoryRepository.updateRepository(inventory)).thenReturn(true)

        // When
        viewModel.updateInventory(inventory)

        // Then
        verify(inventoryRepository).updateRepository(inventory)
        assert(viewModel.progresState.value == false)
    }

    /**
     * Test para updateInventory() cuando ocurre una excepción
     */
    @Test
    fun `updateInventory - excepcion`() = runTest {
        // Given
        val inventory = Inventory(id = "1", name = "Test", price = 100f, quantity = 1)

        `when`(inventoryRepository.updateRepository(inventory)).thenThrow(RuntimeException("Error"))

        // When
        viewModel.updateInventory(inventory)

        // Then
        assert(viewModel.progresState.value == false)
    }

    /**
     * Test para calculateTotalPerProduct()
     * Verifica que se calcule el total por producto correctamente
     */
    @Test
    fun `calculateTotalPerProduct - calcular y actualizar`() = runTest {
        // Given
        val productId = "1"
        val price = 100f
        val quantity = 5
        val expectedTotal = 500f

        `when`(inventoryRepository.updateTotalPerProduct(productId, price, quantity))
            .thenReturn(expectedTotal)

        // When
        viewModel.calculateTotalPerProduct(productId, price, quantity)

        // Then
        verify(inventoryRepository).updateTotalPerProduct(productId, price, quantity)
        assert(viewModel.totalPerProduct.value == expectedTotal)
    }

    /**
     * Test para calculateTotalPerProduct() cuando ocurre una excepción
     */
    @Test
    fun `calculateTotalPerProduct - excepcion`() = runTest {
        // Given
        val productId = "1"
        val price = 100f
        val quantity = 5

        `when`(inventoryRepository.updateTotalPerProduct(productId, price, quantity))
            .thenThrow(RuntimeException("Error"))

        // When
        viewModel.calculateTotalPerProduct(productId, price, quantity)

        // Then
        assert(viewModel.totalPerProduct.value == 0.0f)
    }

    /**
     * Test para deleteProduct()
     * Verifica que se elimine un producto correctamente
     */
    @Test
    fun `deleteProduct - eliminar y actualizar`() = runTest {
        // Given
        val inventory = Inventory(
            id = "1",
            name = "Producto a Eliminar",
            price = 100f,
            quantity = 5,
            total = 500f
        )
        val successMessage = "Producto a Eliminar eliminado correctamente"
        val updatedList = mutableListOf<Inventory>()

        `when`(inventoryRepository.deleteInventory(inventory)).thenReturn(successMessage)
        `when`(inventoryRepository.getListInventory()).thenReturn(updatedList)

        // When
        viewModel.deleteProduct(inventory)

        // Then
        verify(inventoryRepository).deleteInventory(inventory)
        verify(inventoryRepository).getListInventory()
        assert(viewModel.deleteMessage.value == successMessage)
        assert(viewModel.listInventory.value == updatedList)
        assert(viewModel.progresState.value == false)
    }

    /**
     * Test para deleteProduct() cuando ocurre una excepción
     */
    @Test
    fun `deleteProduct - excepcion`() = runTest {
        // Given
        val inventory = Inventory(id = "1", name = "Test", price = 100f, quantity = 1)

        `when`(inventoryRepository.deleteInventory(inventory)).thenThrow(RuntimeException("Error"))

        // When
        viewModel.deleteProduct(inventory)

        // Then
        assert(viewModel.deleteMessage.value == "Error al eliminar")
        assert(viewModel.progresState.value == false)
    }

    
}

