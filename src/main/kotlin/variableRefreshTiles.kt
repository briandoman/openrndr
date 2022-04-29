import org.openrndr.application
import org.openrndr.color.ColorRGBa

import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blend.*
import org.openrndr.extra.fx.blur.*
//import org.openrndr.extra.fx.blend.Normal
import TileVars.TILE_SIZE
import TileVars.COLUMNS
import TileVars.ROWS
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

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
var refreshMap_L1 = mutableMapOf<Int, Int>()
var pageMap_BG = mutableMapOf<Int, ColorRGBa>()
var pageMap_FG = mutableMapOf<Int, ColorRGBa>()
var pageMap_L1 = mutableMapOf<Int, ColorRGBa>()
//var pageMap = mutableMapOf<Int, ColorRGBa>()



fun main() = application {
    configure {
        width = COLUMNS * TILE_SIZE
        height = ROWS * TILE_SIZE
        title = "Panel Squares"
    }

    program {

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

        fun drawLayer(refreshMap: MutableMap<Int, Int>, colorMap: MutableMap<Int, ColorRGBa>, scale: Double, opacity: Double){
            //placeholder
            println("Temp")

        }

        fun drawTileRect(row: Int, column: Int, tileColor: ColorRGBa, scale: Double, opacity: Double): ColorRGBa {
            drawer.fill = tileColor
            drawer.rectangle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble()*scale, TILE_SIZE.toDouble()*scale)
            //drawer.circle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble()/2)
            return tileColor
        }

        fun drawTileCirc(row: Int, column: Int, tileColor: ColorRGBa): ColorRGBa {
            drawer.fill = tileColor
            drawer.circle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble()/2)
            return tileColor
        }

        fun drawPageRect(refreshMap: MutableMap<Int, Int>, pageMap: MutableMap<Int, ColorRGBa>, scale: Double, opacity: Double): MutableMap<Int, ColorRGBa>{
            p = 0
            for (row in 0..ROWS) {
                for (col in 0..COLUMNS) {
                    if (frameCount % (8 * (refreshMap.get(p)!!)) == 0) {
                        // Get random color
                        rndColor = palette_A.random()
                        // update pageMap
                        pageMap.put(p, rndColor)
                        drawTileRect(row, col, rndColor, scale, opacity)
                        //println("PageRect, NEW DRAW | " + frameCount + " | " + (refreshMap.get(p)) + " | " + rndColor.toString() + " | "  )
                    } else {
                        // draw tile
                        drawTileRect(row, col, pageMap_BG.getValue(p), scale, opacity)
                        //println("\tPageRect, REDRAW | " + frameCount + " | " + (refreshMap.get(p)) + " | " + rndColor.toString() + " | "  )
                    }
                    p += 1
                }
            }
            return pageMap
        }

        fun drawPageCirc(refreshMap: MutableMap<Int, Int>, pageMap: MutableMap<Int, ColorRGBa>): MutableMap<Int, ColorRGBa>{
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
                        drawTileCirc(row, col, rndColor)
                        pageMap.put(p, rndColor)

                    } else {
                        // draw tile
                        drawTileCirc(row, col, pageMap.getValue(p))
                        //println("\tPageCirc, REDRAW | " + frameCount + " | " + (refreshMap.get(p)) + " | " + rndColor.toString() + " | "  )
                    }
                    p += 1
                }
            }
            return pageMap
        }

        fun drawStartLayer(colorMap: MutableMap<Int, ColorRGBa>): MutableMap<Int, ColorRGBa> {
            p=0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    panelColor = drawTileRect(row, col, palette_A.random(), 1.0, 1.0)
                    pageMap_BG.put(p, panelColor)
                    p += 1
                }
            }
            return pageMap_BG
        }
        fun drawInitialFrame_BG(): MutableMap<Int, ColorRGBa> {
            pageMap_BG.clear()
            p=0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    panelColor = drawTileRect(row, col, palette_A.random(), 1.0, 1.0)
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

        fun drawInitialFrame(colorMap: MutableMap<Int, ColorRGBa>): MutableMap<Int, ColorRGBa> {
            p=0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    panelColor = drawTileCirc(row, col, palette_A.random())
                    colorMap.put(p, panelColor)
                    p += 1
                }
            }
            return colorMap
        }

        val composite = compose {
            refreshMap_BG = buildRefreshMap(refreshMap_BG)
            refreshMap_FG = buildRefreshMap(refreshMap_FG)
            refreshMap_L1 = buildRefreshMap(refreshMap_L1)
            pageMap_BG = drawInitialFrame(pageMap_BG)
            pageMap_FG = drawInitialFrame(pageMap_FG)
            pageMap_L1 = drawInitialFrame(pageMap_L1)
            draw {
                //pageMap_FG = drawPageCirc(refreshMap_FG, pageMap_FG)
                pageMap_BG = drawPageRect(refreshMap_BG, pageMap_BG, 1.0, 1.0)
                pageMap_L1 = drawPageRect(refreshMap_L1, pageMap_L1, 0.75, 0.5)
            }
            layer {
                blend(Add()) {
                    clip = true
                }
                draw {
                    pageMap_FG = drawPageRect(refreshMap_FG, pageMap_BG, 0.25, 0.5)
                    //pageMap_FG = drawPageCirc(refreshMap_FG, pageMap_FG)
                }

                post(ApproximateGaussianBlur()) {
                    window = 25
                    sigma = cos(seconds) * 10.0 + 2.01

                }
            }
        }

        extend {
            //pageMap = drawPage(refreshMap, pageMap)
            composite.draw(drawer)
        }
    }
}
