import Palettes.palette_A
import Palettes.palette_B
import Palettes.palettes
import TileVars.COLUMNS
import TileVars.ROWS
import TileVars.TILE_SIZE
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blur.GaussianBloom
import kotlin.math.cos
import TileFunctions.makeCoordinates
import org.openrndr.draw.loadImage
import org.openrndr.extra.fx.blend.*

var panelColor = ColorRGBa.BLUE
var coords = listOf<Double>()

val delayList = listOf(1, 2, 8, 32)

var p = 0

var refreshMap_BG = mutableMapOf<Int, Int>()
var refreshMap_FG = mutableMapOf<Int, Int>()
var refreshMap_L1 = mutableMapOf<Int, Int>()
var refreshMap_L2 = mutableMapOf<Int, Int>()

var pageMap_BG = mutableMapOf<Int, ColorRGBa>()
var pageMap_FG = mutableMapOf<Int, ColorRGBa>()
var pageMap_L1 = mutableMapOf<Int, ColorRGBa>()
var pageMap_L2 = mutableMapOf<Int, ColorRGBa>()
//var pageMap = mutableMapOf<Int, ColorRGBa>()

fun main() = application {
    configure {
        width = COLUMNS * TILE_SIZE
        height = ROWS * TILE_SIZE
        title = "Panel Squares"
    }

    program {
        val tileCardChecker = loadImage("data/images/Tile_176_Checker.png")

        fun buildRefreshMap(refreshMap: MutableMap<Int, Int>): MutableMap<Int, Int> {
            p = 0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    refreshMap.put(p, delayList.random())
                    //println(p.toString() + " : " + refreshMap_BG.get(p)  )
                    p += 1
                }
            }
            return refreshMap
        }

        fun buildColorMap(colorMap: MutableMap<Int, ColorRGBa>): MutableMap<Int, ColorRGBa> {
            p=0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    colorMap.put(p, palette_A.random())
                    p += 1
                }
            }
            return colorMap
        }

        fun drawTileRect(row: Int,
                         column: Int,
                         tileColor: ColorRGBa,
                         scale: Double,
                         opacity: Double,
                         pos_x: Double,
                         pos_y: Double): ColorRGBa {
            coords = makeCoordinates(row, column, pos_x, pos_y )
            drawer.fill = tileColor.opacify(opacity)
            drawer.rectangle(coords[0], coords[1], TILE_SIZE*scale, TILE_SIZE*scale)
            return tileColor
        }

        fun drawTileCircSimple(row: Int, column: Int, tileColor: ColorRGBa): ColorRGBa {
            drawer.fill = tileColor
            drawer.circle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble()/2)
            return tileColor
        }

        fun drawTileCirc(row: Int,
                         column: Int,
                         tileColor: ColorRGBa,
                         scale: Double,
                         opacity: Double,
                         pos_x: Double,
                         pos_y: Double): ColorRGBa {
            coords = makeCoordinates(row, column, pos_x, pos_y )
            drawer.fill = tileColor.opacify(opacity)
            drawer.circle(coords[0], coords[1], TILE_SIZE.toDouble() * scale)
            return tileColor
        }

        fun drawPageRect(refreshMap: MutableMap<Int, Int>,
                         colorMap: MutableMap<Int, ColorRGBa>,
                         scale: Double,
                         opacity: Double,
                         pos_x: Double,
                         pos_y: Double,
                         palette: List<List<ColorRGBa>>): MutableMap<Int, ColorRGBa>{
            p = 0
            for (row in 0..ROWS) {
                for (col in 0..COLUMNS) {
                    if (frameCount % (8 * (refreshMap.get(p)!!)) == 0) {
                        // Based on refreshMap this is the case where
                        // we generate a New color, then store it
                        rndColor = palettes.random().random()
                        colorMap.put(p, rndColor)
                        // draw Rectangle
                        drawTileRect(row, col, rndColor, scale, opacity, pos_x, pos_y)
                    } else {
                        drawTileRect(row, col, pageMap_BG.getValue(p), scale, opacity, pos_x, pos_y)
                    }
                    p += 1
                }
            }
            return colorMap
        }

        fun drawPageCircSimple(refreshMap: MutableMap<Int, Int>, pageMap: MutableMap<Int, ColorRGBa>): MutableMap<Int, ColorRGBa>{
            p = 0
            for (row in 0..ROWS) {
                for (col in 0..COLUMNS) {
                    //refreshFactor = refreshMap.get(i)!!
                    if (frameCount % (48 * (refreshMap.get(p)!!)) == 0) {
                        // Get random color
                        // Update frame map
                        // draw new tile
                        rndColor = palette_A.random()
                        //println("PageCirc, NEW DRAW | " + frameCount + " | " + (refreshMap.get(p)) + " | " + rndColor.toString() + " | "  )
                        drawTileCircSimple(row, col, rndColor)
                        pageMap.put(p, rndColor)

                    } else {
                        // draw tile
                        drawTileCircSimple(row, col, pageMap.getValue(p))
                        //println("\tPageCirc, REDRAW | " + frameCount + " | " + (refreshMap.get(p)) + " | " + rndColor.toString() + " | "  )
                    }
                    p += 1
                }
            }
            return pageMap
        }

        fun drawPageCirc(refreshMap: MutableMap<Int, Int>,
                         colorMap: MutableMap<Int, ColorRGBa>,
                         scale: Double,
                         opacity: Double,
                         pos_x: Double,
                         pos_y: Double,
                         palette: List<List<ColorRGBa>>): MutableMap<Int, ColorRGBa>{
            p = 0
            for (row in 0..ROWS) {
                for (col in 0..COLUMNS) {
                    //refreshFactor = refreshMap.get(i)!!
                    if (frameCount % (48 * (refreshMap.get(p)!!)) == 0) {
                        // Get random color
                        // Update frame map
                        // draw new
                        rndColor = palettes.random().random()
                        //println("PageCirc, NEW DRAW | " + frameCount + " | " + (refreshMap.get(p)) + " | " + rndColor.toString() + " | "  )
                        drawTileCirc(row, col, rndColor, scale, opacity, pos_x, pos_y)
                        pageMap.put(p, rndColor)

                    } else {
                        // draw tile
                        drawTileCirc(row, col, pageMap.getValue(p), scale, opacity, pos_x, pos_y)
                        //println("\tPageCirc, REDRAW | " + frameCount + " | " + (refreshMap.get(p)) + " | " + rndColor.toString() + " | "  )
                    }
                    p += 1
                }
            }
            return pageMap
        }


        val composite = compose {
            refreshMap_BG = buildRefreshMap(refreshMap_BG)
            refreshMap_FG = buildRefreshMap(refreshMap_FG)
            refreshMap_L1 = buildRefreshMap(refreshMap_L1)
            refreshMap_L2 = buildRefreshMap(refreshMap_L2)
            pageMap_BG = buildColorMap(pageMap_BG)
            pageMap_FG = buildColorMap(pageMap_FG)
            pageMap_L1 = buildColorMap(pageMap_L1)
            pageMap_L2 = buildColorMap(pageMap_L2)
            draw {
                pageMap_BG = drawPageRect(refreshMap_BG, pageMap_BG, 1.0, 1.0, 0.0, 0.0, palettes)
            }
            layer {
                blend(Normal()) {
                    clip = true
                }
                draw {
                    println("value: " + (frameCount % 60)/60.0 )
                    pageMap_FG = drawPageCirc(refreshMap_FG, pageMap_FG, 0.4, 0.85, (frameCount % 60)/60.0, 0.0, palettes)
                }

                post(GaussianBloom()) {
                    window = 25
                    sigma = cos(seconds /10) + 10.01
                    println(cos(seconds /10) + 1.01)

                }
            }
            layer {
                blend(Multiply()){
                    clip = true
                }
                draw {
                    pageMap_L2 = drawPageCirc(refreshMap_FG, pageMap_FG, 0.2, 0.85, (frameCount % 120)/120.0, 0.0, palettes)
                    //drawer.image(tileCardChecker)
                }
            }
        }

        extend {
            //pageMap = drawPage(refreshMap, pageMap)
            composite.draw(drawer)
        }
    }
}
