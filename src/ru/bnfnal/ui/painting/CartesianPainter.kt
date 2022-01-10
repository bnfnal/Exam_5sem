package ru.bnfnal.ui.painting

import ru.bnfnal.eq
import ru.bnfnal.ge
import ru.bnfnal.le
import ru.bnfnal.neq
import java.awt.*
import kotlin.math.pow
import kotlin.math.roundToInt

// чтобы изменять толщину линий исп
class CartesianPainter(private val plane: CartesianPlane): Painter {
    init{}

    var fontColor:Color  = Color.RED
    var fontSize: Int = 10

    var axesColor: Color = Color.DARK_GRAY

    var xStep: Double = 0.1
    var yStep: Double = 0.1

    var xStepDeg: Double = 1.0
    var yStepDeg: Double = 1.0

    var colorBig: Color = Color.RED
    var colorMedium: Color = Color.BLUE
    var colorSmall: Color = Color.BLACK

    var heightBig: Int = 6
    var heightMedium : Int = 4
    var heightSmall : Int = 2

    var widthBig: Int = 6
    var widthMedium : Int = 4
    var widthSmall : Int = 2

    private fun xScale (x: Double): Double{
        var step = 0.1
        var z: Double = (((((x * 600)/plane.width)*10).roundToInt())/10).toDouble()
        var d = z/100
        xStepDeg = 1.0
        if ( d ge 1.0) { step = (d.roundToInt()).toDouble()}
        else{
            while (d le 1.0){
                d *= 10
                xStepDeg *= 10
            }
            if (d le 1.5){ step = 1/xStepDeg }
            if (d ge 1.5 && d le 3.5 ) { step = 2/xStepDeg }
            if (d ge 3.5 && d le 7.0 ) { step = 5/xStepDeg }
            if (d ge 7.0 ) {
                xStepDeg /= 10
                step = 1/xStepDeg }
        }
        return step
    }

    private fun yScale (y: Double): Double{
        var step = 1.0
        var z: Double = (((((y * 500)/plane.height)*10).roundToInt())/10).toDouble()
        var d = y/100
        yStepDeg = 1.0
        if (d ge 1.0) { step = (d.roundToInt()).toDouble() }
        else{
            while (d le 1.0){
                d *= 10
                yStepDeg *= 10
            }
            if (d le 1.5){ step = 1/yStepDeg }
            if (d ge 1.5 && d le 3.5 ) { step = 2/yStepDeg }
            if (d ge 3.5 && d le 7.0 ) { step = 5/yStepDeg }
            if (d ge 7.0 ) {
                yStepDeg /= 10
                step = 1/yStepDeg
            }
        }
        return step
    }

    private fun xStepRound (x: Double): Double{ return ((x*xStepDeg).roundToInt())/xStepDeg }

    private fun yStepRound (y: Double): Double{ return ((y*yStepDeg).roundToInt())/yStepDeg }


    private fun paintAxes (g: Graphics){
        (g as Graphics2D).apply{
            color = axesColor
            stroke = BasicStroke(1F)
            with(plane){
                if ((xMax le 0.0) || ( xMin ge 0.0))
                {
                    drawLine(0, 0, 0, height)
                    drawLine(width, 0, width, height)
                }
                else
                    g.drawLine(xCrt2Scr(0.0), 0, xCrt2Scr(0.0), height)

                if ((yMax le 0.0) || ( yMin ge 0.0))
                {
                    drawLine(0, 0, width, 0)
                    drawLine(0, height, width, height)
                }
                else
                    drawLine(0, yCrt2Scr(0.0), width, yCrt2Scr(0.0))
            }
        }
    }

    private fun paintTicksX(g: Graphics) {
        (g as Graphics2D).apply{
            stroke = BasicStroke(1F)
            with(plane) {
                xStep = xScale(xMax - xMin)
                val y0: Int = yCrt2Scr(0.0)
                var xmin = xMin
                var xmax = xMax
                var r = xmax
                while ( (xmax*xStepDeg).roundToInt() % (xStep*xStepDeg).roundToInt() != 0){
                    r -= (1/xStepDeg)
                    xmax = xStepRound(r)
                }
                r = xmin
                while ( (xmin*xStepDeg).roundToInt() % (xStep*xStepDeg).roundToInt() != 0){
                    r += (1/xStepDeg)
                    xmin = xStepRound(r)
                }
                var xL = (xmin / xStep).roundToInt()
                var xR = (xmax / xStep).roundToInt()
                var h: Int
                var xI: Int
                for (i in 0..xR - xL) {
                    xI = xCrt2Scr(xStepRound(xmin + xStep * i))
                    color = colorSmall
                    h = heightSmall
                    if ((xL + i) % 10 == 0) {
                        color = colorBig
                        h = heightBig
                    }
                    if ((xL + i) % 10 != 0 && (xL + i) % 5 == 0) {
                        color = colorMedium
                        h = heightMedium
                    }
                    if ((yMax le 0.0) || (yMin ge 0.0)) {
                        drawLine(xI, 0, xI, h)
                        drawLine(xI, height - h, xI, height)
                    } else
                        drawLine(xI, y0 - h, xI, y0 + h)
                }
            }
        }
    }

    private fun paintTicksY(g: Graphics){
        (g as Graphics2D).apply{
            stroke = BasicStroke(1F)
            with(plane){
                yStep = yScale(yMax - yMin)
                val x0: Int = xCrt2Scr(0.0)
                var ymin = yMin
                var ymax = yMax
                var r = ymax
                while ( (ymax*yStepDeg).roundToInt() % (yStep*yStepDeg).roundToInt() != 0){
                    r -= (1/yStepDeg)
                    ymax = xStepRound(r)
                }
                r = ymin
                while ( (ymin*yStepDeg).roundToInt() % (yStep*yStepDeg).roundToInt() != 0){
                    r += (1/yStepDeg)
                    ymin = xStepRound(r)
                }
                var yU = (ymax / yStep).roundToInt()
                var yD = (ymin / yStep).roundToInt()
                var w: Int
                var yI: Int
                for (i in 0..yU - yD)
                {
                    yI = yCrt2Scr(yStepRound(ymin + yStep * i))
                    color = colorSmall
                    w = widthSmall
                    if ((yD + i) % 10 == 0){
                        color = colorBig
                        w = widthBig
                    }
                    if ((yD + i) % 10 != 0 && (yD + i) % 5 == 0){
                        color = colorMedium
                        w = widthMedium
                    }
                    if ((xMax le 0.0) || ( xMin ge 0.0))
                    {
                        drawLine(0, yI, w, yI)
                        drawLine(width - w, yI, width, yI)
                    }
                    else
                        drawLine(x0 - w, yI, x0 + w, yI)
                }
            }
        }
    }

    private fun paintLabelsX(g: Graphics) {
        (g as Graphics2D).apply{
            stroke = BasicStroke(1F)
            color = fontColor
            font = Font("CaLibri", Font.BOLD, fontSize)
            with(plane) {
                xStep = xScale(xMax - xMin)
                val y0: Int = yCrt2Scr(0.0)
                var xmin = xMin
                var xmax = xMax
                var r = xmax
                while ( (xmax*xStepDeg).roundToInt() % (xStep*xStepDeg).roundToInt() != 0){
                    r -= (1/xStepDeg)
                    xmax = xStepRound(r)
                }
                r = xmin
                while ( (xmin*xStepDeg).roundToInt() % (xStep*xStepDeg).roundToInt() != 0){
                    r += (1/xStepDeg)
                    xmin = xStepRound(r)
                }
                var xL = (xmin / xStep).roundToInt()
                var xR = (xmax / xStep).roundToInt()
                var x: Double
                var t: Float
                for (i in 0..xR - xL) {
                    x = xStepRound(xmin + xStep * i)
                    if ((xL + i) % 10 == 0) {
                        val (tW, tH) = fontMetrics.getStringBounds(x.toString(), g).run { Pair(width.toFloat(), height.toFloat()) }
                        t = tW /2F
                        if (x eq 0.0){ t = -(widthBig + 3).toFloat() }
                        if (x eq xmin){ t = -1F }
                        if (x eq xmax){ t = tW + 1 }
                        if ((yMax le 0.0) || (yMin ge 0.0)) {
                            drawString(x.toString(), xCrt2Scr(x) - t, tH + heightBig)
                            drawString(x.toString(), xCrt2Scr(x) - t, (height - heightBig - 3).toFloat())
                        } else
                            drawString(x.toString(), xCrt2Scr(x) - t, y0 + tH + heightBig)
                    }
                }
            }
        }
    }

    private fun paintLabelsY(g: Graphics){
        (g as Graphics2D).apply{
            stroke = BasicStroke(1F)
            color = fontColor
            with(plane){
                yStep = yScale(yMax - yMin)
                val x0: Int = xCrt2Scr(0.0)
                var ymin = yMin
                var ymax = yMax
                var r = ymax
                while ( (ymax*yStepDeg).roundToInt() % (yStep*yStepDeg).roundToInt() != 0){
                    r -= (1/yStepDeg)
                    ymax = xStepRound(r)
                }
                r = ymin
                while ( (ymin*yStepDeg).roundToInt() % (yStep*yStepDeg).roundToInt() != 0){
                    r += (1/yStepDeg)
                    ymin = xStepRound(r)
                }
                var yU = (ymax / yStep).roundToInt()
                var yD = (ymin / yStep).roundToInt()
                var y: Double
                var t: Float
                for (i in 0..yU - yD)
                {
                    y = yStepRound(ymin + yStep * i)
                    if ((yD + i) % 10 == 0 && y neq 0.0){
                        val (tW, tH) = fontMetrics.getStringBounds(y.toString(), g).run { Pair(width.toFloat(), height.toFloat()) }
                        t = tH/2F - 3
                        if (y eq ymin){ t = -1F }
                        if (y eq ymax){ t = tH - 4 }
                        if ((xMax le 0.0) || ( xMin ge 0.0))
                        {
                            drawString(y.toString(), (widthBig + 3).toFloat(), yCrt2Scr(y) + t)
                            drawString(y.toString(), (width - widthBig - tW - 3).toFloat(), yCrt2Scr(y) + t)
                        }
                        else
                            drawString(y.toString(), (x0 + widthBig + 3).toFloat(),yCrt2Scr(y) + t)
                    }
                }
            }
        }
    }
    
    override fun paint(g: Graphics){
        paintAxes(g)
        paintTicksX(g)
        paintTicksY(g)
        paintLabelsX(g)
        paintLabelsY(g)
    }
}
