package ru.bnfnal.ui.painting

import java.awt.*

class FunctionPainter(private val plane: CartesianPlane, var function: (Double) -> Double): Painter {

    var funColor: Color = Color.BLUE

    override  fun paint(g: Graphics){
        with(g as Graphics2D){
            color = funColor
            stroke = BasicStroke(3F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
            var rh = mapOf(
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,  // сглаживание - включено, на границах цвет по-бледнее
                RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY, // помедленнее, но зато покачественнее
                RenderingHints.KEY_DITHERING to RenderingHints.VALUE_DITHER_ENABLE
            )

            with(plane){
                for (i in 0 until width){
                    drawLine(
                        i,
                        yCrt2Scr(function(xScr2Crt(i))),
                        i+1,
                        yCrt2Scr(function(xScr2Crt(i+1)))
                    )
                }
            }
        }

    }
}