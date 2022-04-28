import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur

import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blend.Add
//import org.openrndr.extra.fx.blend.Normal
import kotlin.math.cos


val NUM_COLS = 8
val NUM_ROWS = 4
val TILE_SIZE = 176

val color_A = ColorRGBa.fromHex("#191A19")
val color_B = ColorRGBa.fromHex("#1E5128")
val color_C = ColorRGBa.fromHex("#4E9F3D")
val color_D = ColorRGBa.fromHex("#D8E9A8")

val palette_A = listOf(color_A, color_B, color_C, color_D)
var panelColor = ColorRGBa.BLUE
val delayList = listOf(1, 2, 8, 32)

var p = 0

var refreshMap = mutableMapOf<Int, Int>()
var pageMap = mutableMapOf<Int, ColorRGBa>()


fun main() = application {
    configure {
        width = NUM_COLS * TILE_SIZE
        height = NUM_ROWS * TILE_SIZE
        title = "Panel Squares"
    }

    program {

        fun buildRefreshMap(): MutableMap<Int, Int> {
            p = 0
            for (row in 0..NUM_ROWS){
                for (col in 0..NUM_COLS){
                    refreshMap.put(p, delayList.random())
                    println(p.toString() + " : " + refreshMap.get(p)  )
                    p += 1
                }
            }
            return refreshMap
        }

        fun drawTile(row: Int, column: Int, tileColor: ColorRGBa): ColorRGBa {
            drawer.fill = tileColor
            //drawer.rectangle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble(), TILE_SIZE.toDouble())
            drawer.circle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble()/2)
            return tileColor
        }

        fun drawPage(refreshMap: MutableMap<Int, Int>, frameMap: MutableMap<Int, ColorRGBa>): MutableMap<Int, ColorRGBa>{
            p = 0
            for (row in 0..NUM_ROWS) {
                for (col in 0..NUM_COLS) {
                    //refreshFactor = refreshMap.get(i)!!
                    if (frameCount % (48 * (refreshMap.get(p)!!)) == 0) {
                        // Get random color
                        // Update frame map
                        // draw new tile
                        rndColor = palette_A.random()
                        frameMap.put(p, rndColor)
                        drawTile(row, col, rndColor)
                    } else {
                        // draw tile
                        drawTile(row, col, frameMap.getValue(p))
                    }
                    p += 1
                }
            }
            return frameMap
        }

        fun drawInitialFrame(): MutableMap<Int, ColorRGBa> {
            pageMap.clear()
            p=0
            for (row in 0..NUM_ROWS){
                for (col in 0..NUM_COLS){
                    panelColor = drawTile(row, col, palette_A.random())
                    pageMap.put(p, panelColor)
                    p += 1
                }
            }
            return pageMap
        }

        val composite = compose {
            draw {
                //drawer.fill, etc.
                //drawer.stroke, etc
                //drawer.rectangle, etc
                pageMap = drawPage(refreshMap, pageMap)

            }

            layer {
                blend(Add()) {
                    clip = true
                }
                draw {
                    drawer.fill = ColorRGBa.PINK
                    drawer.stroke = null
                    drawer.circle(width / 2.0, height / 2.0 + cos(seconds * 2) * 100.0, 100.0)

                }
                post(ApproximateGaussianBlur()) {
                    window = 25
                    sigma = cos(seconds) * 10.0 + 10.01

                }
            }
        }


        refreshMap = buildRefreshMap()
        pageMap = drawInitialFrame()

        extend {
            //pageMap = drawPage(refreshMap, pageMap)
            composite.draw(drawer)
        }
    }
}
