import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur

import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blend.Add
//import org.openrndr.extra.fx.blend.Normal
import kotlin.math.cos
import TileVars.TILE_SIZE
import TileVars.COLUMNS
import TileVars.ROWS


//val NUM_COLS = 8
//val NUM_ROWS = 4
//val TILE_SIZE = 176

val color_A = ColorRGBa.fromHex("#191A19")
val color_B = ColorRGBa.fromHex("#1E5128")
val color_C = ColorRGBa.fromHex("#4E9F3D")
val color_D = ColorRGBa.fromHex("#D8E9A8")

val palette_A = listOf(color_A, color_B, color_C, color_D)
var panelColor = ColorRGBa.BLUE
val delayList = listOf(1, 2, 8, 32)

var p = 0

var refreshMap_BG = mutableMapOf<Int, Int>()
var refreshMap_FG = mutableMapOf<Int, Int>()
var pageMap_BG = mutableMapOf<Int, ColorRGBa>()
var pageMap_FG = mutableMapOf<Int, ColorRGBa>()



fun main() = application {
    configure {
        width = COLUMNS * TILE_SIZE
        height = ROWS * TILE_SIZE
        title = "Panel Squares"
    }

    program {

        fun buildRefreshMap_BG(): MutableMap<Int, Int> {
            p = 0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    refreshMap_BG.put(p, delayList.random())
                    println(p.toString() + " : " + refreshMap_BG.get(p)  )
                    p += 1
                }
            }
            return refreshMap_BG
        }

        fun buildRefreshMap_FG(): MutableMap<Int, Int> {
            p = 0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    refreshMap_FG.put(p, delayList.random())
                    println(p.toString() + " : " + refreshMap_FG.get(p)  )
                    p += 1
                }
            }
            return refreshMap_FG
        }

        fun drawTileRect(row: Int, column: Int, tileColor: ColorRGBa): ColorRGBa {
            drawer.fill = tileColor
            drawer.rectangle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble(), TILE_SIZE.toDouble())
            //drawer.circle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble()/2)
            return tileColor
        }

        fun drawTileCirc(row: Int, column: Int, tileColor: ColorRGBa): ColorRGBa {
            drawer.fill = tileColor
            //drawer.rectangle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble(), TILE_SIZE.toDouble())
            drawer.circle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble()/2)
            return tileColor
        }

        fun drawPageRect(refreshMap: MutableMap<Int, Int>, pageMap_BG: MutableMap<Int, ColorRGBa>): MutableMap<Int, ColorRGBa>{
            p = 0
            for (row in 0..ROWS) {
                for (col in 0..COLUMNS) {
                    //refreshFactor = refreshMap.get(i)!!
                    if (frameCount % (48 * (refreshMap.get(p)!!)) == 0) {
                        // Get random color
                        // Update frame map
                        // draw new tile
                        rndColor = palette_A.random()
                        pageMap_BG.put(p, rndColor)
                        drawTileRect(row, col, rndColor)
                    } else {
                        // draw tile
                        drawTileRect(row, col, pageMap_BG.getValue(p))
                    }
                    p += 1
                }
            }
            return pageMap_BG
        }

        fun drawPageCirc(refreshMap: MutableMap<Int, Int>, frameMap_FG: MutableMap<Int, ColorRGBa>): MutableMap<Int, ColorRGBa>{
            p = 0
            for (row in 0..ROWS) {
                for (col in 0..COLUMNS) {
                    //refreshFactor = refreshMap.get(i)!!
                    if (frameCount % (48 * (refreshMap.get(p)!!)) == 0) {
                        // Get random color
                        // Update frame map
                        // draw new tile
                        rndColor = palette_A.random()
                        frameMap_FG.put(p, rndColor)
                        drawTileCirc(row, col, rndColor)
                    } else {
                        // draw tile
                        drawTileCirc(row, col, frameMap_FG.getValue(p))
                    }
                    p += 1
                }
            }
            return frameMap_FG
        }

        fun drawInitialFrame_BG(): MutableMap<Int, ColorRGBa> {
            pageMap_BG.clear()
            p=0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    panelColor = drawTileRect(row, col, palette_A.random())
                    pageMap_BG.put(p, panelColor)
                    p += 1
                }
            }
            return pageMap_BG
        }

        fun drawInitialFrame_FG(): MutableMap<Int, ColorRGBa> {
            pageMap_FG.clear()
            p=0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    panelColor = drawTileCirc(row, col, palette_A.random())
                    pageMap_FG.put(p, panelColor)
                    p += 1
                }
            }
            return pageMap_FG
        }

        val composite = compose {
            draw {
                //pageMap_BG = drawPageRect(refreshMap_BG, pageMap_BG)
                pageMap_FG = drawPageCirc(refreshMap_FG, pageMap_FG)
            }

            layer {
                blend(Add()) {
                    clip = true
                }
                draw {
                    //drawer.fill = ColorRGBa.PINK
                    //drawer.stroke = null
                    pageMap_FG = drawPageCirc(refreshMap_FG, pageMap_FG)
                    //drawer.circle(width / 2.0, height / 2.0 + cos(seconds * 2) * 100.0, 100.0)

                }
                post(ApproximateGaussianBlur()) {
                    window = 25
                    sigma = cos(seconds) * 10.0 + 10.01

                }
            }
        }


        refreshMap_BG = buildRefreshMap_BG()
        refreshMap_FG = buildRefreshMap_FG()
        pageMap_BG = drawInitialFrame_BG()
        pageMap_FG = drawInitialFrame_FG()

        extend {
            //pageMap = drawPage(refreshMap, pageMap)
            composite.draw(drawer)
        }
    }
}
