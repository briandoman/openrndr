import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.draw.tint
import kotlin.math.cos
import kotlin.math.sin

val COLS = 8
val ROWS = 4
val TILE = 176

val colorA = ColorRGBa.fromHex("#191A19")
val colorB = ColorRGBa.fromHex("#1E5128")
val colorC = ColorRGBa.fromHex("#4E9F3D")
val colorD = ColorRGBa.fromHex("#D8E9A8")

val pallete_a = listOf(colorA, colorB, colorC, colorD)
var rndColor = colorB
var tileColor = ColorRGBa.BLUE
var delays = listOf(1.0, 2.0, 4.0, 8.0)


var counter = 0
var i = 0

var frameMap = mutableMapOf<Int, ColorRGBa>()
//var delay = False

fun main() = application {
    configure {
        width = COLS * TILE.toInt()
        height = ROWS * TILE.toInt()
        title = "Panel Squares"
    }

    program {
        val image = loadImage("data/images/pm5544.png")
        val font = loadFont("data/fonts/default.otf", 64.0)
        rndColor = pallete_a.random()

        fun drawNewTile(row: Int, column: Int): ColorRGBa {
            rndColor = pallete_a.random()
            drawer.fill = rndColor
            drawer.rectangle(column.toDouble() * TILE, row.toDouble() * TILE, TILE.toDouble(), TILE.toDouble())
            return rndColor
        }

        fun redrawTile(row: Int, column: Int, color: ColorRGBa){
            drawer.fill = color
            drawer.rectangle(column.toDouble() * TILE, row.toDouble() * TILE, TILE.toDouble(), TILE.toDouble())
        }

        fun redrawFrame(frameMap: MutableMap<Int, ColorRGBa>){
            i = 0
            for (row in 0..ROWS){
                for (col in 0..COLS){
                    tileColor = frameMap.getValue(i)
                    redrawTile(row, col, tileColor)
                    frameMap.get(i)
                    i += 1
                }
            }
        }


        fun drawNewFrame(): MutableMap<Int, ColorRGBa> {
            frameMap.clear()
            i=0
            for (row in 0..ROWS){
                for (col in 0..COLS){
                    rndColor = drawNewTile(row, col)
                    frameMap.put(i, rndColor)
                    println("\tStoring Map : " + i.toString() + " : " + rndColor)
                    i += 1
                }
            }
            return frameMap
        }
        frameMap = drawNewFrame()

        extend {

            if (frameCount % 60 == 0) {
                frameMap = drawNewFrame()
                println(frameCount.toString() + " : Drawing New Frame Now")
                println("\t" + frameMap)
            } else {
                redrawFrame(frameMap)
                //println(frameCount.toString() + " : Redrawing Frame Now")
                println(frameCount.toString() + frameMap)
            }
        }
    }
}
