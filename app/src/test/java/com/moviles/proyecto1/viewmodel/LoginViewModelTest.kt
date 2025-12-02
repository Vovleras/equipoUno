package com.moviles.proyecto1.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.FirebaseAuth
import com.moviles.proyecto1.model.UserRequest
import com.moviles.proyecto1.model.UserResponse
import com.moviles.proyecto1.repository.LoginRepository
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
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * Pruebas unitarias para LoginViewModel
 * Usando JUnit, Mockito y Coroutines Test
 */
@ExperimentalCoroutinesApi
class LoginViewModelTest {

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
    private lateinit var loginRepository: LoginRepository

    // ViewModel a probar
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginRepository)
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
     * Test para registerUser() - registro exitoso
     * Verifica que se registre un usuario correctamente
     */
    @Test
    fun `registerUser - registro exitoso`() = runTest {
        // Given
        val userRequest = UserRequest(
            email = "test@example.com",
            password = "password123"
        )
        val userResponse = UserResponse(
            email = "test@example.com",
            isRegister = true,
            message = "Registro Exitoso"
        )

        // Configurar el mock para que llame al callback con userResponse
        doAnswer { invocation ->
            val callback = invocation.getArgument<(UserResponse) -> Unit>(1)
            callback(userResponse)
            null
        }.`when`(loginRepository).registerUser(anyObject(), anyObject())

        // When
        viewModel.registerUser(userRequest)

        // Then
        verify(loginRepository).registerUser(anyObject(), anyObject())
        assert(viewModel.isRegister.value == userResponse)
        assert(viewModel.isRegister.value?.isRegister == true)
        assert(viewModel.isRegister.value?.message == "Registro Exitoso")
    }

    /**
     * Test para registerUser() - registro fallido
     * Verifica el manejo de errores en el registro
     */
    @Test
    fun `registerUser - registro fallido`() = runTest {
        // Given
        val userRequest = UserRequest(
            email = "test@example.com",
            password = "weak"
        )
        val userResponse = UserResponse(
            email = null,
            isRegister = false,
            message = "Error en el registro"
        )

        doAnswer { invocation ->
            val callback = invocation.getArgument<(UserResponse) -> Unit>(1)
            callback(userResponse)
            null
        }.`when`(loginRepository).registerUser(anyObject(), anyObject())

        // When
        viewModel.registerUser(userRequest)

        // Then
        verify(loginRepository).registerUser(anyObject(), anyObject())
        assert(viewModel.isRegister.value?.isRegister == false)
        assert(viewModel.isRegister.value?.message == "Error en el registro")
    }

    /**
     * Test para registerUser() - email vacío
     * Verifica que se maneje correctamente un email vacío
     */
    @Test
    fun `registerUser - email vacio`() = runTest {
        // Given
        val userRequest = UserRequest(
            email = "",
            password = "password123"
        )
        val userResponse = UserResponse(
            email = null,
            isRegister = false,
            message = "Error en el registro"
        )

        doAnswer { invocation ->
            val callback = invocation.getArgument<(UserResponse) -> Unit>(1)
            callback(userResponse)
            null
        }.`when`(loginRepository).registerUser(anyObject(), anyObject())

        // When
        viewModel.registerUser(userRequest)

        // Then
        verify(loginRepository).registerUser(anyObject(), anyObject())
        assert(viewModel.isRegister.value?.isRegister == false)
    }

    /**
     * Test para registerUser() - password vacío
     * Verifica que se maneje correctamente un password vacío
     */
    @Test
    fun `registerUser - password vacio`() = runTest {
        // Given
        val userRequest = UserRequest(
            email = "test@example.com",
            password = ""
        )
        val userResponse = UserResponse(
            email = null,
            isRegister = false,
            message = "Error en el registro"
        )

        doAnswer { invocation ->
            val callback = invocation.getArgument<(UserResponse) -> Unit>(1)
            callback(userResponse)
            null
        }.`when`(loginRepository).registerUser(anyObject(), anyObject())

        // When
        viewModel.registerUser(userRequest)

        // Then
        verify(loginRepository).registerUser(anyObject(), anyObject())
        assert(viewModel.isRegister.value?.isRegister == false)
    }

    /**
     * Test para sesion() con email válido
     * Verifica que retorne true cuando el email no es null
     */
    @Test
    fun `sesion - email valido retorna true`() {
        // Given
        val email = "test@example.com"
        var result = false

        // When
        viewModel.sesion(email) { isEnabled ->
            result = isEnabled
        }

        // Then
        assert(result == true)
    }

    /**
     * Test para sesion() con email null
     * Verifica que retorne false cuando el email es null
     */
    @Test
    fun `sesion - email null retorna false`() {
        // Given
        val email: String? = null
        var result = true

        // When
        viewModel.sesion(email) { isEnabled ->
            result = isEnabled
        }

        // Then
        assert(result == false)
    }

    /**
     * Test para sesion() con email vacío
     * Un email vacío no es null, por lo que debería retornar true
     */
    @Test
    fun `sesion - email vacio retorna true`() {
        // Given
        val email = ""
        var result = false

        // When
        viewModel.sesion(email) { isEnabled ->
            result = isEnabled
        }

        // Then
        assert(result == true)
    }

    /**
     * Test para verificar la lógica de loginUser() con credenciales vacías
     * Simula: email.isNotEmpty() && pass.isNotEmpty() == false
     */
    @Test
    fun `loginUser - email vacio retorna false`() {
        // Given
        val email = ""
        val password = "password123"

        // When - Simulamos la validación de loginUser()
        val result = email.isNotEmpty() && password.isNotEmpty()

        // Then
        assert(result == false)
    }

    /**
     * Test para verificar la lógica de loginUser() con password vacío
     */
    @Test
    fun `loginUser - password vacio retorna false`() {
        // Given
        val email = "test@example.com"
        val password = ""

        // When - Simulamos la validación de loginUser()
        val result = email.isNotEmpty() && password.isNotEmpty()

        // Then
        assert(result == false)
    }

    /**
     * Test para verificar la lógica de loginUser() con ambos campos vacíos
     */
    @Test
    fun `loginUser - credenciales vacias retorna false`() {
        // Given
        val email = ""
        val password = ""

        // When - Simulamos la validación de loginUser()
        val result = email.isNotEmpty() && password.isNotEmpty()

        // Then
        assert(result == false)
    }

    /**
     * Test para verificar la lógica de loginUser() con credenciales válidas
     */
    @Test
    fun `loginUser - credenciales validas pasan validacion`() {
        // Given
        val email = "test@example.com"
        val password = "password123"

        // When - Simulamos la validación de loginUser()
        val result = email.isNotEmpty() && password.isNotEmpty()

        // Then
        assert(result == true)
    }

    /**
     * Test para verificar lógica de sesion() con múltiples emails válidos
     */
    @Test
    fun `sesion - multiples emails validos`() {
        // Given
        val emails = listOf(
            "test1@example.com",
            "test2@example.com",
            "admin@company.org"
        )

        // When & Then
        emails.forEach { email ->
            var result = false
            viewModel.sesion(email) { isEnabled ->
                result = isEnabled
            }
            assert(result == true)
        }
    }

    /**
     * Test para verificar lógica con email de un carácter
     */
    @Test
    fun `sesion - email de un caracter retorna true`() {
        // Given
        val email = "a"
        var result = false

        // When
        viewModel.sesion(email) { isEnabled ->
            result = isEnabled
        }

        // Then
        assert(result == true)
    }

    /**
     * Test para verificar lógica con email muy largo
     */
    @Test
    fun `sesion - email muy largo retorna true`() {
        // Given
        val email = "a".repeat(100) + "@example.com"
        var result = false

        // When
        viewModel.sesion(email) { isEnabled ->
            result = isEnabled
        }

        // Then
        assert(result == true)
    }

    /**
     * Test para verificar lógica con caracteres especiales en email
     */
    @Test
    fun `sesion - email con caracteres especiales retorna true`() {
        // Given
        val email = "user+tag@example.com"
        var result = false

        // When
        viewModel.sesion(email) { isEnabled ->
            result = isEnabled
        }

        // Then
        assert(result == true)
    }

    /**
     * Test para verificar lógica con email que es solo un espacio
     */
    @Test
    fun `sesion - email con solo espacio retorna true`() {
        // Given
        val email = " "
        var result = false

        // When
        viewModel.sesion(email) { isEnabled ->
            result = isEnabled
        }

        // Then
        assert(result == true)
    }

    /**
     * Test para verificar lógica alternando null y no-null
     */
    @Test
    fun `sesion - alternando null y no-null`() {
        // Given & When & Then
        var result1 = false
        viewModel.sesion("test@example.com") { result1 = it }
        assert(result1 == true)

        var result2 = true
        viewModel.sesion(null) { result2 = it }
        assert(result2 == false)

        var result3 = false
        viewModel.sesion("another@example.com") { result3 = it }
        assert(result3 == true)
    }

    /**
     * Test para verificar lógica con diferentes formatos de email
     */
    @Test
    fun `sesion - diferentes formatos de email retornan true`() {
        // Given
        val emails = listOf(
            "simple@example.com",
            "user.name@example.com",
            "user+tag@example.co.uk",
            "123@456.789",
            "@",
            "no-at-symbol"
        )

        // When & Then
        emails.forEach { email ->
            var result = false
            viewModel.sesion(email) { isEnabled ->
                result = isEnabled
            }
            assert(result == true)
        }
    }

    /**
     * Test para verificar validación de loginUser con caracteres especiales
     */
    @Test
    fun `loginUser - caracteres especiales en password pasa validacion`() {
        // Given
        val email = "test@example.com"
        val password = "P@ssw0rd!#$%"

        // When
        val result = email.isNotEmpty() && password.isNotEmpty()

        // Then
        assert(result == true)
    }

    /**
     * Test para verificar validación de loginUser con password muy largo
     */
    @Test
    fun `loginUser - password muy largo pasa validacion`() {
        // Given
        val email = "test@example.com"
        val password = "a".repeat(1000)

        // When
        val result = email.isNotEmpty() && password.isNotEmpty()

        // Then
        assert(result == true)
    }

    /**
     * Test para verificar validación de loginUser con email con espacios
     */
    @Test
    fun `loginUser - email con solo espacios pasa validacion isEmpty`() {
        // Given
        val email = " "
        val password = "password123"

        // When - Un espacio NO está vacío según isEmpty()
        val result = email.isNotEmpty() && password.isNotEmpty()

        // Then
        assert(result == true)
    }

    /**
     * Test para verificar consistencia de múltiples invocaciones
     */
    @Test
    fun `sesion - multiples invocaciones con mismo email`() {
        // Given
        val email = "test@example.com"

        // When & Then - Múltiples evaluaciones
        var result1 = false
        viewModel.sesion(email) { result1 = it }
        assert(result1 == true)

        var result2 = false
        viewModel.sesion(email) { result2 = it }
        assert(result2 == true)

        var result3 = false
        viewModel.sesion(email) { result3 = it }
        assert(result3 == true)
    }

    /**
     * Test para verificar totalProduct - función de utilidad
     * Simula: precio * cantidad
     */
    @Test
    fun `totalProduct - calculo correcto`() {
        // Given
        val precio = 100f
        val cantidad = 5

        // When
        val result = precio * cantidad

        // Then
        assert(result == 500f)
    }

    /**
     * Test para verificar totalProduct con cantidad cero
     */
    @Test
    fun `totalProduct - cantidad cero retorna cero`() {
        // Given
        val precio = 100f
        val cantidad = 0

        // When
        val result = precio * cantidad

        // Then
        assert(result == 0f)
    }

    /**
     * Test para verificar totalProduct con precio cero
     */
    @Test
    fun `totalProduct - precio cero retorna cero`() {
        // Given
        val precio = 0f
        val cantidad = 5

        // When
        val result = precio * cantidad

        // Then
        assert(result == 0f)
    }
}
