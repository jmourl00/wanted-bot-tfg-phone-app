package com.example.wanted_app

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.wanted_app.ui.theme.*
import androidx.compose.foundation.interaction.MutableInteractionSource

data class PlataformaInfo(
    val nombre: String,
    val colorFondo: Color,
    val colorTexto: Color
)

val plataformas = mapOf(
    "Vinted" to PlataformaInfo("Vinted", VintedBg, VintedText),
    "Wallapop" to PlataformaInfo("Wallapop", WallapopBg, WallapopText),
    "eBay" to PlataformaInfo("eBay", EbayBg, EbayText),
    "Milanuncios" to PlataformaInfo("Milanuncios", MilanunciosBg, MilanunciosText)
)

private val TARJETA_ALTURA = 130.dp

@Composable
fun PantallaProductos() {
    var filtroActivo by remember { mutableStateOf("Todos") }
    var vistaCompacta by remember { mutableStateOf(false) }
    var productosList by remember {
        mutableStateOf(
            listOf(
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
        )
    }

    val productosFiltrados = if (filtroActivo == "Todos") productosList
    else productosList.filter { it.plataforma == filtroActivo }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Productos", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { vistaCompacta = !vistaCompacta }) {
                    Icon(
                        if (vistaCompacta) Icons.Default.List else Icons.Default.Menu,
                        contentDescription = "Cambiar vista",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filtros = listOf("Todos") + plataformas.keys.toList()
                items(filtros) { filtro ->
                    FilterChip(
                        selected = filtroActivo == filtro,
                        onClick = { filtroActivo = filtro },
                        label = { Text(filtro, fontSize = 13.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        Text(
            "${productosFiltrados.size} productos encontrados",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(if (vistaCompacta) 4.dp else 10.dp),
            modifier = Modifier.animateContentSize()
        ) {
            items(productosFiltrados) { producto ->
                if (vistaCompacta) {
                    TarjetaCompacta(
                        producto = producto,
                        onDescartar = {
                            productosList = productosList.filter { it.id != producto.id }
                        }
                    )
                } else {
                    TarjetaProductoNueva(
                        producto = producto,
                        onDescartar = {
                            productosList = productosList.filter { it.id != producto.id }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaProductoNueva(producto: Producto, onDescartar: () -> Unit) {
    val info = plataformas[producto.plataforma]
    var mostrarChat by remember { mutableStateOf(false) }
    var mostrarGaleria by remember { mutableStateOf(false) }
    // Estados: null = ninguno activo, "descartar" o "comprar" = primer click hecho
    var botonActivo by remember { mutableStateOf<String?>(null) }

    if (mostrarGaleria) {
        Dialog(onDismissRequest = { mostrarGaleria = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Imagen ampliada", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(producto.imagenes) { img ->
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("IMG", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "${producto.imagenes.size} fotos",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    if (mostrarChat) {
        Dialog(onDismissRequest = { mostrarChat = false }) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(8.dp)) {
                    TextButton(
                        onClick = { mostrarChat = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Negociar precio más bajo", fontSize = 14.sp)
                    }
                    HorizontalDivider()
                    TextButton(
                        onClick = { mostrarChat = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Preguntar acerca del producto", fontSize = 14.sp)
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(TARJETA_ALTURA)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                // Tocar cualquier parte de la tarjeta deselecciona el botón activo
                botonActivo = null
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Barra de plataforma
            if (info != null) {
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .fillMaxHeight()
                        .background(
                            info.colorFondo,
                            RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        info.nombre,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = info.colorTexto,
                        modifier = Modifier.textoVertical(),
                        maxLines = 1
                    )
                }
            }

            // Imagen
            Box(
                modifier = Modifier
                    .width(TARJETA_ALTURA)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        botonActivo = null
                        mostrarGaleria = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    producto.plataforma.first().toString(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (producto.imagenes.size > 1) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .background(
                                Color.Black.copy(alpha = 0.55f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            "${producto.imagenes.size}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Contenido derecho
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 12.dp, end = 10.dp, top = 10.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Fila superior
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            producto.nombre,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            String.format("%.2f EUR", producto.precio),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = if (MaterialTheme.colorScheme.background == BackgroundDark)
                                PrecioColorDark else PrecioColor
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape
                            )
                            .clickable { botonActivo = null },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "i",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Fila inferior
                Column {
                    Text(
                        producto.busqueda,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Chat
                        FilledTonalIconButton(
                            onClick = {
                                botonActivo = null
                                mostrarChat = true
                            },
                            modifier = Modifier.size(34.dp),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = SecondaryLight
                            )
                        ) {
                            Text(
                                "\u2026",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Secondary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        // Descartar - doble click
                        val descartarActivo = botonActivo == "descartar"
                        FilledTonalIconButton(
                            onClick = {
                                if (descartarActivo) {
                                    botonActivo = null
                                    onDescartar()
                                } else {
                                    botonActivo = "descartar"
                                }
                            },
                            modifier = Modifier.size(34.dp),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = if (descartarActivo)
                                    Color(0xFFE24B4A).copy(alpha = 0.3f)
                                else Color(0xFFFCEBEB)
                            )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Descartar",
                                tint = if (descartarActivo)
                                    Color(0xFFE24B4A)
                                else Favorito.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        // Comprar - doble click
                        val comprarActivo = botonActivo == "comprar"
                        FilledTonalIconButton(
                            onClick = {
                                if (comprarActivo) {
                                    botonActivo = null
                                    // Aquí irá la lógica de compra automática
                                } else {
                                    botonActivo = "comprar"
                                }
                            },
                            modifier = Modifier.size(34.dp),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = if (comprarActivo)
                                    Color(0xFF0F6E56).copy(alpha = 0.3f)
                                else VintedBg
                            )
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Comprar",
                                tint = if (comprarActivo)
                                    Color(0xFF0F6E56)
                                else VintedText.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaCompacta(producto: Producto, onDescartar: () -> Unit) {
    val info = plataformas[producto.plataforma]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barra plataforma mini
            if (info != null) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(
                            info.colorFondo,
                            RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Imagen mini
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    producto.plataforma.first().toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Nombre y búsqueda
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    producto.nombre,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    producto.busqueda,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    maxLines = 1
                )
            }

            // Precio
            Text(
                String.format("%.0f EUR", producto.precio),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (MaterialTheme.colorScheme.background == BackgroundDark)
                    PrecioColorDark else PrecioColor,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Descartar
            IconButton(onClick = onDescartar, modifier = Modifier.size(24.dp)) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Descartar",
                    tint = Favorito,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            // Comprar
            IconButton(onClick = { }, modifier = Modifier.size(24.dp)) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Comprar",
                    tint = VintedText,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

// Texto vertical rotado para la barra de plataforma
fun Modifier.textoVertical() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.height, placeable.width) {
        placeable.place(
            x = -(placeable.width / 2 - placeable.height / 2),
            y = -(placeable.height / 2 - placeable.width / 2)
        )
    }
}