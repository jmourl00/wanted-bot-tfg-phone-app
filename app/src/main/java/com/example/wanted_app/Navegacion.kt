package com.example.wanted_app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wanted_app.ui.theme.WantedAppTheme

sealed class Pantalla(val ruta: String, val titulo: String, val icono: ImageVector) {
    object Inicio : Pantalla("inicio", "Inicio", Icons.Default.Home)
    object Busquedas : Pantalla("busquedas", "Búsquedas", Icons.Default.Search)
    object Productos : Pantalla("productos", "Productos", Icons.Default.ShoppingCart)
    object Favoritos : Pantalla("favoritos", "Favoritos", Icons.Default.Favorite)
    object Perfil : Pantalla("perfil", "Perfil", Icons.Default.Person)
}

data class BotonBarra(val icono: ImageVector, val ruta: String)

@Composable
fun AppPrincipal() {
    WantedAppTheme {
        val navController = rememberNavController()

        val botonesBarra = listOf(
            BotonBarra(Icons.Default.Person, "perfil"),
            BotonBarra(Icons.Default.Settings, "config_app"),
            BotonBarra(Icons.Default.Build, "config_bot"),
            BotonBarra(Icons.Default.ShoppingCart, "productos"),
        )

        Scaffold(
            bottomBar = {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.navigationBarsPadding() // Mantiene a salvo la barra del sistema
                    ) {
                        HorizontalDivider(thickness = 0.6.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                // Aquí está la magia:
                                // top y bottom pequeños = barra menos alta
                                // end pequeño = elementos más pegados a la derecha
                                .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 6.dp),
                            horizontalArrangement = Arrangement.End, // Mantiene los elementos a la derecha
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            botonesBarra.forEachIndexed { index, boton ->
                                // Tus botones aquí
                                IconButton(onClick = { navController.navigate(boton.ruta) }) {
                                    Icon(imageVector = boton.icono, contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Pantalla.Inicio.ruta,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Pantalla.Inicio.ruta) {
                    PantallaInicio(
                        onNavegar = { ruta ->
                            navController.navigate(ruta) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
                composable(Pantalla.Busquedas.ruta) { PantallaBusquedas() }
                composable(Pantalla.Productos.ruta) { PantallaProductos() }
                composable(Pantalla.Favoritos.ruta) { PantallaFavoritos() }
                composable(Pantalla.Perfil.ruta) { PantallaPerfil() }
                composable("config_bot") { PantallaConfigBot() }
                composable("config_app") { PantallaConfigApp() }
            }
        }
    }
}