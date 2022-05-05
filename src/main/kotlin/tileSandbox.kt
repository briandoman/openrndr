/*
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


fun main() = application {
    configure {
        width = COLUMNS * TILE_SIZE
        height = ROWS * TILE_SIZE
        title = "SANDBOX"
    }

    program {

        }

        val composite = compose {

            draw {
                //pageMap_FG = drawPageCirc(refreshMap_FG, pageMap_FG)
                pageMap_BG = drawPageRect(refreshMap_BG, pageMap_BG, 1.0, 1.0)
                pageMap_L1 = drawPageRect(refreshMap_L1, pageMap_L1, 0.75, 0.5)
            }
            layer {
                blend(Normal()) {
                    clip = true
                }
                draw {
                    //pageMap_FG = drawPageRect(refreshMap_FG, pageMap_BG, 0.25, 0.5)
                    pageMap_FG = drawPageCircScaled(refreshMap_FG, pageMap_FG, 0.4, 0.85, palette_A)
                }

                post(GaussianBloom()) {
                    window = 25
                    sigma = cos(seconds /10) + 4.01
                    println(cos(seconds /10) + 1.01)

                }
            }
            layer {
                blend(Add()){
                    clip = true
                }
                draw {
                    pageMap_L2 = drawPageCircScaled(refreshMap_FG, pageMap_FG, 0.2, 0.85, palette_B)
                }
            }
        }

        extend {
            //pageMap = drawPage(refreshMap, pageMap)
            composite.draw(drawer)
        }
    }
}
*/