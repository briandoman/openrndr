import org.openrndr.application
import org.openrndr.shape.Rectangle
import org.openrndr.draw.loadImage
import org.openrndr.shape.IntRectangle

import TileVars.TILE_SIZE
import TileVars.COLUMNS
import TileVars.ROWS

val CROPX = 0
val CROPY = 900
var FRAME = 0

fun main() = application {
    configure {
        width = COLUMNS * TILE_SIZE
        height = ROWS * TILE_SIZE
    }

    program {
        val sourceImage = loadImage("data/images/190806_PLATES_0028.png")
        //val tileBlur = BoxBlur()
        /*
        val comp_A = compose {
            println("this is only executed once")
            draw
        }
        */
        extend {
            for (row in 0..ROWS){
                for (column in 0..COLUMNS){
                    var cropRegion = IntRectangle((CROPX + (column * row * 24) + (FRAME / 360)), (CROPY), TILE_SIZE, TILE_SIZE)
                    var imageCrop = sourceImage.crop(cropRegion)
                    //var blurBuffer = colorBuffer(imageCrop.width, imageCrop.height)
                    var sourceRect = Rectangle(0.0, 0.0, TILE_SIZE.toDouble(), TILE_SIZE.toDouble())
                    var targetRect = Rectangle((TILE_SIZE.toDouble() * column), (TILE_SIZE.toDouble() * row), TILE_SIZE.toDouble(), TILE_SIZE.toDouble())

                    //tileBlur.window = (cos((seconds / 6) * Math.PI) * 4.0 + 5.0).toInt()
                    //tileBlur.apply(imageCrop, blurBuffer)

                    drawer.image(imageCrop, sourceRect, targetRect)
                    FRAME += 1
                }
            }
        }
    }
}
