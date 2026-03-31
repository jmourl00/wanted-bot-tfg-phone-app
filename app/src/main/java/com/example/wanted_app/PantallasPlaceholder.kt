package com.example.wanted_app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaBusquedas() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Búsquedas activas", fontSize = 20.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun PantallaFavoritos(viewModel: ProductosViewModel) {
    val favoritos = viewModel.favoritos

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Favoritos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 4.dp)
        )
        Text(
            "${favoritos.size} guardados",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)
        )

        if (favoritos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No tienes productos guardados",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(favoritos) { producto ->
                    TarjetaProductoNueva(
                        producto = producto,
                        onDescartar = { viewModel.descartar(producto.id) },
                        onFavorito = { viewModel.toggleFavorito(producto.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaPerfil() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Perfil", fontSize = 20.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun PantallaConfigBot() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Configuración del Bot", fontSize = 20.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun PantallaConfigApp() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Configuración de la App", fontSize = 20.sp, fontWeight = FontWeight.Medium)
    }
}
