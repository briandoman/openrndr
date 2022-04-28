import org.openrndr.application
import org.openrndr.color.ColorRGBa

val NUM_COLS = 8
val NUM_ROWS = 4
val TILE_SIZE = 176

val color_A = ColorRGBa.fromHex("#191A19")
val color_B = ColorRGBa.fromHex("#1E5128")
val color_C = ColorRGBa.fromHex("#4E9F3D")
val color_D = ColorRGBa.fromHex("#D8E9A8")

val palette_A = listOf(color_A, color_B, color_C, color_D)
var tileColor = ColorRGBa.BLUE
val delayList = listOf(1, 2, 4, 8)

var i = 0

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
            i = 0
            for (row in 0..NUM_ROWS){
                for (col in 0..NUM_COLS){
                    refreshMap.put(i, delayList.random())
                    println(i.toString() + " : " + refreshMap.get(i)  )
                    i += 1
                }
            }
            return refreshMap
        }

        fun drawTile(row: Int, column: Int, tileColor: ColorRGBa): ColorRGBa {
            drawer.fill = tileColor
            drawer.rectangle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble(), TILE_SIZE.toDouble())
            return tileColor
        }

        fun drawPage(refreshMap: MutableMap<Int, Int>, frameMap: MutableMap<Int, ColorRGBa>): MutableMap<Int, ColorRGBa>{
            i = 0
            for (row in 0..NUM_ROWS) {
                for (col in 0..NUM_COLS) {
                    //refreshFactor = refreshMap.get(i)!!
                    if (frameCount % (60 * (refreshMap.get(i)!!)) == 0) {
                        // Get random color
                        // Update frame map
                        // draw new tile
                        rndColor = palette_A.random()
                        frameMap.put(i, rndColor)
                        drawTile(row, col, rndColor)
                    } else {
                        // draw tile
                        drawTile(row, col, frameMap.getValue(i))
                    }
                    i += 1
                }
            }
            return frameMap
        }

        fun drawInitialFrame(): MutableMap<Int, ColorRGBa> {
            pageMap.clear()
            i=0
            for (row in 0..NUM_ROWS){
                for (col in 0..NUM_COLS){
                    tileColor = drawTile(row, col, palette_A.random())
                    pageMap.put(i, tileColor)
                    i += 1
                }
            }
            return pageMap
        }

        refreshMap = buildRefreshMap()
        pageMap = drawInitialFrame()

        extend {
            pageMap = drawPage(refreshMap, pageMap)
        }
    }
}
