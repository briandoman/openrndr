package TileFunctions

import TileVars.TILE_SIZE
import org.openrndr.color.ColorRGBa
import TileVars.TILE_SIZE
import TileVars.ROWS
import TileVars.COLUMNS

fun makeCoordinates(row: Int, col: Int, pos_x: Double, pos_y: Double): List<Double> {
    return listOf((col * TILE_SIZE + (TILE_SIZE * pos_x)), (row * TILE_SIZE + (TILE_SIZE * pos_y)))
}




