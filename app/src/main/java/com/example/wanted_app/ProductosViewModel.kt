package com.example.wanted_app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ProductosViewModel : ViewModel() {
    private val _productos = mutableStateListOf(
        Producto("1", "Nike Air Max 90", 45.00, "Vinted",
            tiempoDetectado = "Hace 2 min",
            busqueda = "zapatillas nike",
            imagenes = listOf("img1", "img2", "img3")),
        Producto("2", "PS5 Slim Digital", 320.00, "Wallapop",
            tiempoDetectado = "Hace 5 min",
            busqueda = "ps5",
            imagenes = listOf("img1", "img2")),
        Producto("3", "MacBook Pro M2 2023", 890.00, "eBay",
            tiempoDetectado = "Hace 12 min",
            busqueda = "macbook pro",
            imagenes = listOf("img1")),
        Producto("4", "iPhone 14 Pro 128GB", 550.00, "Milanuncios",
            tiempoDetectado = "Hace 18 min",
            busqueda = "iphone 14",
            imagenes = listOf("img1", "img2", "img3", "img4")),
        Producto("5", "AirPods Pro 2", 120.00, "Vinted",
            tiempoDetectado = "Hace 25 min",
            busqueda = "airpods",
            imagenes = listOf("img1", "img2")),
        Producto("6", "Nintendo Switch OLED", 230.00, "Wallapop",
            tiempoDetectado = "Hace 30 min",
            busqueda = "nintendo switch",
            imagenes = listOf("img1"))
    )

    val productos: List<Producto> get() = _productos
    val favoritos: List<Producto> get() = _productos.filter { it.esFavorito }

    fun toggleFavorito(id: String) {
        val index = _productos.indexOfFirst { it.id == id }
        if (index != -1) {
            _productos[index] = _productos[index].copy(esFavorito = !_productos[index].esFavorito)
        }
    }

    fun descartar(id: String) {
        _productos.removeAll { it.id == id }
    }
}