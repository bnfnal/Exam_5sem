package ru.bnfnal.ui.painting
import ru.bnfnal.ge
import ru.bnfnal.le
import java.awt.*
import kotlin.math.roundToInt

class PointsPainter(private val plane: CartesianPlane): Painter {

    var pointsColor: Color = Color.GREEN
    val deltaScr = 10

    var pointsCrt: MutableList<Pair<Double, Double>> = mutableListOf()
        private set

    // мы кликаем мышкой по эрану выбирая точки, а потом по ним строим график полинома

    override  fun paint(g: Graphics){
        with(g as Graphics2D){
            color = pointsColor
            stroke = BasicStroke(3F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
            var rh = mapOf(
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,  // сглаживание - включено, на границах цвет по-бледнее
                RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY, // помедленнее, но зато по-качественнее
                RenderingHints.KEY_DITHERING to RenderingHints.VALUE_DITHER_ENABLE
            )
            with(plane){
                pointsCrt.forEach(){
                    fillOval(xCrt2Scr(it.first)-deltaScr/2, yCrt2Scr(it.second)-deltaScr/2, deltaScr, deltaScr)
                }
            }
        }


    }

    fun add(p: Pair<Int, Int>){
        val xPointsStep = xPointsScale(plane.xMax - plane.xMin)
        var k = true
        val new = Pair(plane.xScr2Crt(p.first), plane.yScr2Crt(p.second))
        for (it in 0 until  pointsCrt.size){
            if (Math.abs(new.first - pointsCrt[it].first) le xPointsStep){
                k = false
                break
            }
        }
        if ( k == true ) { pointsCrt.add(new) }
    }

    fun delete(p: Pair<Int, Int>){
        val xPointsStep = xPointsScale(plane.xMax - plane.xMin)
        val yPointsStep = yPointsScale(plane.yMax - plane.yMin)
        val new = Pair(plane.xScr2Crt(p.first), plane.yScr2Crt(p.second))
        var k = -1
        for (it in 0 until pointsCrt.size){
            if ((Math.abs(new.first - pointsCrt[it].first) le xPointsStep) && (Math.abs(new.second - pointsCrt[it].second) le yPointsStep)){
                k = it
                break
            }
        }
        if ( k >= 0 ) {
            pointsCrt.removeAt(k) }
    }


    private fun xPointsScale (x: Double): Double{
        var step = 0.1
        var z: Double = (((((x * 600)/plane.width)*10).roundToInt())/10).toDouble()
        var d = z/100
        var xStepDeg = 1.0
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

    private fun yPointsScale (y: Double): Double{
        var step = 1.0
        var z: Double = (((((y * 500)/plane.height)*10).roundToInt())/10).toDouble()
        var d = y/100
        var yStepDeg = 1.0
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
}