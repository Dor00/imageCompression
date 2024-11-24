package co.edu.udea.compumovil.gr01_20242.pickerpacker.viewMenu

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb



data class QuadtreeNode(
    var color: Int = Color.Transparent.toArgb(), // Utiliza `Color.Transparent.toArgb()` para convertir a entero
    var isLeaf: Boolean = true,
    var children: Array<QuadtreeNode?>? = null
)