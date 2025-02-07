package ec.edu.epn.rq_driver.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.epn.rq_driver.api.RetrofitInstance
import ec.edu.epn.rq_driver.model.Ruta
import ec.edu.epn.rq_driver.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RutaViewModel : ViewModel() {

    private val _rutas = MutableStateFlow<List<Ruta>>(emptyList())
    val rutas: StateFlow<List<Ruta>> = _rutas

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios

    private val _mensajeCrearRuta = MutableStateFlow<String?>(null)
    val mensajeCrearRuta: StateFlow<String?> = _mensajeCrearRuta

    private val _esExito = MutableStateFlow<Boolean?>(null) // true si éxito, false si error
    val esExito: StateFlow<Boolean?> = _esExito

    // Crear una nueva ruta
    fun crearRuta(ruta: Ruta) {
        viewModelScope.launch {
            try {
                val rutaCreada = RetrofitInstance.rutaApi.crearRuta(ruta)
                // Aquí puedes actualizar el estado, dependiendo de lo que necesites hacer después de crear la ruta
                _rutas.value = _rutas.value.toMutableList().apply { add(rutaCreada) }
                _esExito.value = true
                _mensajeCrearRuta.value = "Ruta creada con éxito"
            } catch (e: Exception) {
                Log.e("RutaViewModel", "Error al crear la ruta: ${e.message}")
                _mensajeCrearRuta.value = "Error al crear la ruta"
                _esExito.value = false
            }
        }
    }

    // Obtener rutas por driverId
    fun obtenerRutas(driverId: String) {
        viewModelScope.launch {
            try {
                val rutaApi = RetrofitInstance.rutaApi
                val rutasObtenidas = rutaApi.obtenerRutas(driverId) // Pasamos el driverId en la URL
                _rutas.value = rutasObtenidas
            } catch (e: Exception) {
                Log.e("RutaViewModel", "Error al obtener rutas: ${e.message}")
            }
        }
    }

    fun obtenerUsuariosRuta(rutaId: String) {
        viewModelScope.launch {
            try {
                val rutaApi = RetrofitInstance.rutaApi
                val usuariosRuta = rutaApi.usuariosRuta(rutaId)
                _usuarios.value = usuariosRuta
            } catch (e: Exception) {
                Log.e("RutaViewModel", "Error al obtener rutas: ${e.message}")
            }
        }
    }

    fun limpiarMensajeCrearRuta() {
        _mensajeCrearRuta.value = null
    }

    fun limpiarExito() {
        _esExito.value = null
    }
}
