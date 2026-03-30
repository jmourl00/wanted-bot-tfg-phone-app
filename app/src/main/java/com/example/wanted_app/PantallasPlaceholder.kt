package com.example.wanted_app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun PantallaBusquedas() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Búsquedas activas", fontSize = 20.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun PantallaFavoritos() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Favoritos", fontSize = 20.sp, fontWeight = FontWeight.Medium)
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