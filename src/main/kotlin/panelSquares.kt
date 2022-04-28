import org.openrndr.application
import org.openrndr.color.ColorRGBa
import kotlin.random.Random
import TileVars.TILE_SIZE
import TileVars.COLUMNS
import TileVars.ROWS

val colorA = ColorRGBa.fromHex("#191A19")
val colorB = ColorRGBa.fromHex("#1E5128")
val colorC = ColorRGBa.fromHex("#4E9F3D")
val colorD = ColorRGBa.fromHex("#D8E9A8")

val pallete_a = listOf(color_A, color_B, color_C, color_D)
var rndColor = color_B
var tileColor = ColorRGBa.BLUE
var delays = listOf(1.0, 2.0, 4.0, 8.0)


var counter = 0
var ii = 0

var delayMap = mutableMapOf<Int, Boolean>()
var pageMap = mutableMapOf<Int, ColorRGBa>()

fun main() = application {
    configure {
        width = COLUMNS * TILE_SIZE
        height = COLUMNS * TILE_SIZE
        title = "Panel Squares"
    }

    program {
        //val image = loadImage("data/images/pm5544.png")
        //val font = loadFont("data/fonts/default.otf", 64.0)
        rndColor = palette_A.random()

        fun buildDelayMap(): MutableMap<Int, Boolean> {
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    //assign randomness to a bool
                    if (Random.nextDouble() <= 0.4) {
                        delayMap.put(ii, true)
                        println("storing delayMap index : " + ii.toString() + " : 1")
                    } else {
                        delayMap.put(ii, false)
                        println("storing delayMap index : " + ii.toString() + " : 0")
                    }
                }
            }
            return delayMap
        }

        fun drawNewTile(row: Int, column: Int): ColorRGBa {
            rndColor = palette_A.random()
            drawer.fill = rndColor
            drawer.rectangle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble(), TILE_SIZE.toDouble())
            return rndColor
        }

        fun redrawTile(row: Int, column: Int, color: ColorRGBa){
            drawer.fill = color
            drawer.rectangle(column.toDouble() * TILE_SIZE, row.toDouble() * TILE_SIZE, TILE_SIZE.toDouble(), TILE_SIZE.toDouble())
        }

        fun drawFrameWithDelay(frameMap: MutableMap<Int, ColorRGBa>, delayMap: MutableMap<Int, Boolean>){
            ii = 0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    if (delayMap.get(ii)==true){
                        tileColor = frameMap.getValue(ii)
                        redrawTile(row, col, tileColor)
                        frameMap.get(ii)
                    }
                    ii += 1
                }
            }
        }

        fun redrawFrame(frameMap: MutableMap<Int, ColorRGBa>){
            ii = 0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    tileColor = frameMap.getValue(ii)
                    redrawTile(row, col, tileColor)
                    frameMap.get(ii)
                    ii += 1
                }
            }
        }


        fun drawNewFrame(): MutableMap<Int, ColorRGBa> {
            pageMap.clear()
            ii=0
            for (row in 0..ROWS){
                for (col in 0..COLUMNS){
                    rndColor = drawNewTile(row, col)
                    pageMap.put(ii, rndColor)
                    //println("\tStoring Map : " + i.toString() + " : " + rndColor)
                    ii += 1
                }
            }
            return pageMap
        }

        pageMap = drawNewFrame()

        extend {

            if (frameCount % 60 == 0) {
                pageMap = drawNewFrame()
                println(frameCount.toString() + " : Drawing New Frame Now")
                println("\t" + pageMap)
            } else {
                redrawFrame(pageMap)
                //println(frameCount.toString() + " : Redrawing Frame Now")
                println(frameCount.toString() + pageMap)
            }
        }
    }
}
