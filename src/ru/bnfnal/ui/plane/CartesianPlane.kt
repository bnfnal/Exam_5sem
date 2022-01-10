package ru.bnfnal.ui.painting

import ru.bnfnal.eq
import java.awt.Dimension
import java.lang.Math.max
import kotlin.math.roundToInt

class CartesianPlane(
    // параметры конструктора, после выхода из конструктора они исчезнут
    // мы не объявили их свойствами сразу, тк иначе они были бы публичными свойствами сразу
    // чтобы при изменении границ мы могли изменить эти свойства, а не вызывать конструктор заново
    // эти параметры могут принимать вообще любые значения
    // а нам нужно, чтобы xMin < xMax
    // наш класс будет исп в других проектах
    // значит нужно самим делать эту проверку(не зависимо от спинеров)
    // именно поэтому нужны спец свойства
    xMin: Double,
    xMax: Double,
    yMin: Double,
    yMax: Double
) {

    var tMin: Double = -1000.0
    var tMax: Double = -1000.0
    var xFunction: (Double)-> Double = {it -> it}
    var yFunction: (Double)-> Double = {it -> it}
    // размер плоскости в пикселях
    var pixelSize: Dimension = Dimension(1, 1)
        set(size){
            field = size
            width = field.width
            height = field.height
        }

    // ширина и высота панели в пикселях
    private var xSize: Int = 1
    private var ySize: Int = 1

    // пиксель с номером width и height уже лежать вне видимой области панели
    // мы видим только пиксели от 0 до width-1 и от 0 до height-1
    // width и height мы будем исп при рисовании
    var width: Int
        get() = xSize - 1
        set(value) {
            xSize = max(1, value)
        }

    var height: Int
        get() = ySize - 1
        set(value) {
            ySize = max(1, value)
        }

    // чтобы всегда выполнялось условие xMin < xMax, будем задавать их попарно
    var xMin: Double = 0.0
        private set
    var xMax: Double = 0.0
        private set
    var yMin: Double = 0.0
        private set
    var yMax: Double = 0.0
        private set

    var xSegment: Pair<Double, Double>
        get() = Pair(xMin, xMax)
        set(value){
            val k = if (value.first eq value.second) 0.1 else 0.0
//            if (value.first <= value.second){
//                xMin = value.first - k
//                xMax = value.second + k
//            }
//            else{
//                xMin = value.second
//                xMax = value.first
//            }
            xMin = value.first - k
            xMax = value.second + k
            if (xMin > xMax) xMin = xMax.also{ xMax = xMin }
        }

    var ySegment: Pair<Double, Double>
        get() = Pair(yMin, yMax)
        set(value){
            val k = if (value.first eq value.second) 0.1 else 0.0
            yMin = value.first - k
            yMax = value.second + k
            if (yMin > yMax) yMin = yMax.also{ yMax = yMin }
        }

    var tSegment: Pair<Double, Double>
        get() = Pair(tMin, tMax)
        set(value){
            val k = if (value.first eq value.second) 0.1 else 0.0
            tMin = value.first - k
            tMax = value.second + k
            if (tMin > tMax) tMin = tMax.also{ tMax = tMin }
            xMin = (((xFunction(tMin))*10).roundToInt())/10.0
            xMax = (((xFunction(tMax))*10).roundToInt())/10.0
            yMin = (((yFunction(tMin))*10).roundToInt())/10.0
            yMax = (((yFunction(tMax))*10).roundToInt())/10.0
        }

    // плотность пикселей на единичном отрезке, сколько пикселей содержит единичный отрезок
    /**
     * Плотность пикселей на единичном отрезке по оси x

     */
    val xDen: Double
        get() = width/(xMax - xMin)

    val yDen: Double
        get() = height/(yMax - yMin)

    init{
        xSegment = Pair(xMin, xMax)
        ySegment = Pair(yMin, yMax)
    }

    constructor(t_min: Double, t_max:Double, x_function: (Double) -> Double, y_function: (Double) -> Double):
            this(((x_function(t_min)*10).roundToInt())/10.0,
                ((x_function(t_max)*10).roundToInt())/10.0,
                ((y_function(t_min)*10).roundToInt())/10.0,
                ((y_function(t_max)*10).roundToInt())/10.0)
    {
        tMin = t_min
        tMax = t_max
        xFunction = x_function
        yFunction = y_function
    }

    // функции преобразования декарт сист коррдинат в экранную и обратно по х и по у
    // из декартовой в экранную (получ веществ число, возвращ целое)

    // документация к функции, она будет выходить в виде подсказки даже из других файлов
    // @see ссылка на какую-то другую функцию

    /**
     * Преобразование абсциссы из декартовой системы координат в экранную
     *
     * @param x абсциса точки в декартовой системе координат
     * @return абсцисса точки в экранной системе координат
     */
    fun xCrt2Scr(x: Double): Int{
        var r = (xDen * (x - xMin)).toInt()
        if (r < -width ) r = -width
        if (r > 2 * width) r = 2 * width
        return r
    }

    fun xScr2Crt(x: Int): Double{
        var r = x / xDen + xMin
        if (r < xMin ) r = xMin
        if (r > xMax) r = xMax
        return r
    }

    fun yCrt2Scr(y: Double): Int{
        var r = height - (yDen * (y - yMin)).toInt()
        if (r < -height ) r = -height
        if (r > 2 * height) r = 2 * height
        return r
    }

    fun yScr2Crt(y: Int): Double{
        var r = ((height - y) / yDen) + yMin
        if (r < yMin ) r = yMin
        if (r > yMax) r = yMax
        return r
    }
}