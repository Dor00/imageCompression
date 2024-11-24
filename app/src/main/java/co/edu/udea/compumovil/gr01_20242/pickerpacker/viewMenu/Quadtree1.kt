package co.edu.udea.compumovil.gr01_20242.pickerpacker.viewMenu

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// clase del quadtree
class Quadtree1 {
    private var root: QuadtreeNode? = null

    init {
        root = QuadtreeNode()
    }
    // Método para construir el QuadTree
    fun buildQuadtree(image: Bitmap, maxDepth: Int): QuadtreeNode? {
        root = buildQuadtreeRecursive(image, maxDepth, 0, 0, image.width, image.height)
        return root
    }
    // metodo recursivo para construir el quadtree
    private fun buildQuadtreeRecursive(image: Bitmap, maxDepth: Int, x: Int, y: Int, width: Int, height: Int): QuadtreeNode? {
        return if (maxDepth == 0 || isHomogeneous(image, x, y, width, height)) {
            val leafNode = QuadtreeNode()
            leafNode.color = calculateAverageColor(image, x, y, width, height)
            leafNode.isLeaf = true
            leafNode
        } else {
            val parentNode = QuadtreeNode()
            parentNode.isLeaf = false
            parentNode.children = arrayOfNulls(4)

            val subWidth = width / 2
            val subHeight = height / 2

            parentNode.children!![0] = buildQuadtreeRecursive(image, maxDepth - 1, x, y, subWidth, subHeight) // Top left
            parentNode.children!![1] = buildQuadtreeRecursive(image, maxDepth - 1, x + subWidth, y, subWidth, subHeight) // Top right
            parentNode.children!![2] = buildQuadtreeRecursive(image, maxDepth - 1, x, y + subHeight, subWidth, subHeight) // Bottom left
            parentNode.children!![3] = buildQuadtreeRecursive(image, maxDepth - 1, x + subWidth, y + subHeight, subWidth, subHeight) // Bottom right

            return parentNode
        }
    }
    // metodo para verificar si el nodo es homogeneo
    private fun isHomogeneous(image: Bitmap, x: Int, y: Int, width: Int, height: Int): Boolean {
        val firstColor = image.getPixel(x, y)
        for (i in x until x + width) {
            for (j in y until y + height) {
                if (image.getPixel(i, j) != firstColor) {
                    return false
                }
            }
        }
        return true
    }
    // Función para calcular el color promedio
    private fun calculateAverageColor(image: Bitmap, x: Int, y: Int, width: Int, height: Int): Int {
        var totalRed = 0
        var totalGreen = 0
        var totalBlue = 0
        var pixelCount = 0

        for (i in x until x + width) {
            for (j in y until y + height) {
                val pixelColor = image.getPixel(i, j)
                totalRed += (pixelColor shr 16) and 0xFF
                totalGreen += (pixelColor shr 8) and 0xFF
                totalBlue += pixelColor and 0xFF
                pixelCount++
            }
        }

        val avgRed = totalRed / pixelCount
        val avgGreen = totalGreen / pixelCount
        val avgBlue = totalBlue / pixelCount

        // Utiliza Color.rgb para crear un color a partir de RGB
        return Color(avgRed, avgGreen, avgBlue).toArgb()
    }
    // metodo para reconstruir la imagen
    fun reconstructImage(rootNode: QuadtreeNode?, width: Int, height: Int): Bitmap {
        val reconstructedImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        reconstructImageRecursive(rootNode, reconstructedImage, 0, 0, width, height)
        return reconstructedImage
    }
    // metodo recursivo para reconstruir la imagen
    private fun reconstructImageRecursive(node: QuadtreeNode?, image: Bitmap, x: Int, y: Int, width: Int, height: Int) {
        if (node == null) return
        if (node.isLeaf) {
            for (i in x until x + width) {
                for (j in y until y + height) {
                    image.setPixel(i, j, node.color)
                }
            }
        } else {
            val subWidth = width / 2
            val subHeight = height / 2

            reconstructImageRecursive(node.children!![0], image, x, y, subWidth, subHeight) // Top left
            reconstructImageRecursive(node.children!![1], image, x + subWidth, y, subWidth, subHeight) // Top right
            reconstructImageRecursive(node.children!![2], image, x, y + subHeight, subWidth, subHeight) // Bottom left
            reconstructImageRecursive(node.children!![3], image, x + subWidth, y + subHeight, subWidth, subHeight) // Bottom right
        }
    }

}
