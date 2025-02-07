package ec.edu.epn.rq_driver.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.epn.rq_driver.api.ConductorApi
import ec.edu.epn.rq_driver.api.RetrofitInstance
import ec.edu.epn.rq_driver.components.ErrorState
import ec.edu.epn.rq_driver.model.Conductor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class PerfilViewModel : ViewModel() {

    private val apiDelPerfil = MutableStateFlow<ConductorApi?>(null)

    private val _usuariosEncontrados = MutableStateFlow<List<Conductor>?>(emptyList())
    val usuariosEncontrados: StateFlow<List<Conductor>?> = _usuariosEncontrados

    private val _usuarioActual = MutableStateFlow<Conductor?>(null)
    val usuarioActual: StateFlow<Conductor?> = _usuarioActual

    private val _mensajeDeSuscripcion = MutableStateFlow<String?>(null)
    val mensajeDeSuscripcion: StateFlow<String?> = _mensajeDeSuscripcion


    private fun obtenerAPI() {
        try {
            apiDelPerfil.value = RetrofitInstance.conductorApi
            Log.d("ConductorViewModel", "Creación correcta de API: ${apiDelPerfil.value}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(
                "ConductorViewModel",
                "Problema al crear la API: ${e.message}"
            )
        }
    }

    private fun <T> cargarDatos(
        objeto: MutableStateFlow<T?> = MutableStateFlow(null),
        callback: suspend (() -> T?)): T?
    {

        Log.d("ConductorViewModel", "Iniciando carga de datos...")

        var resultado: T? = null

        viewModelScope.launch {
            try {
                resultado = callback.invoke()
                objeto.value = resultado
                Log.d("ConductorViewModel", "Carga exitosa !\n\t$resultado")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(
                    "ConductorViewModel",
                    "Problema al cargar los datos (${e.message})"
                )
            }
        }

        return resultado

    }


    init {
        obtenerAPI()
    }


    val encontrarUsuarioPorCorreo: (String) -> Conductor? = { email ->
        cargarDatos(_usuarioActual) { apiDelPerfil.value?.obtenerUsuarioPorCorreo(email) }
    }

    val encontrarUsuarioPorId: (String) -> Conductor? = { id ->
        cargarDatos(_usuarioActual) { apiDelPerfil.value?.obtenerPerfilDelConductor(id) }
    }

    val cargarListaDeUsuarios: () -> List<Conductor> = {
        cargarDatos(_usuariosEncontrados) { apiDelPerfil.value?.obtenerUsuarios() } ?: emptyList()
    }

    fun registrarConductor(conductor: Conductor) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = RetrofitInstance.conductorApi.crearNuevoUsuario(conductor)
                if (res.isSuccessful) {
                    Log.d("ConductorViewModel", "Conductor registrado con éxito (${res.message()})")
                    _mensajeDeSuscripcion.value = "Suscripción exitosa"
                } else {
                    Log.d("ConductorViewModel",
                        res.errorBody()?.string()?.trim()?.let { JSONObject(it).toString() } ?: res.errorBody().toString()
                    )
                    _mensajeDeSuscripcion.value = "Error en el registro (${res.message()})"
                }
            } catch (e: Exception) {
                Log.e("ConductorViewModel", "Error al registrar usuario", e)
                _mensajeDeSuscripcion.value = "Error en el registro"
            }
        }
    }

    fun validarCampo(input: String): ErrorState {
        return when {
            input.trim().isEmpty() -> ErrorState(true, "Este campo es requerido")
            else -> ErrorState(false)
        }
    }

    fun validarCorreo(input: String): ErrorState {

        val emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.[a-z]+\\.?[a-z]+".toRegex()

        return when {
            input.trim().isEmpty() -> ErrorState(true, "Debe llenar el campo de correo")
            !( input.trim().matches(emailPattern) ) -> ErrorState(true, "El formato del correo no es correcto")
            else -> ErrorState(false)
        }

    }

    fun validarPassword(input: String): ErrorState {
        return when {
            input.trim().isEmpty() -> ErrorState(true, "Debe llenar el campo de Contraseña")
            input.trim().length < 6 -> ErrorState(true, "La contraseña debe contener al menos 6 carácteres")
            else -> ErrorState(false)
        }
    }

    fun validarConfirmarPassword(input1: String, input2: String): ErrorState {
        return when {
            input2.trim().isEmpty() -> ErrorState(true, "Debe llenar el campo de Confirmar Contraseña")
            input1.trim() != input2.trim() -> ErrorState(true, "Las contraseñas no coinciden")
            else -> ErrorState(false)
        }
    }

}
