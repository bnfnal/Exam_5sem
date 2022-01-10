package ru.bnfnal.ui.polynoms
import ru.bnfnal.eq
import ru.bnfnal.neq
import kotlin.math.abs

import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt

open class Polynom(coeff: Collection<Double>) {    // полином выделен желтым, тк не опред ф-ция хэш код

    var variableName: String = "x"

    private val _coeff: MutableList<Double> = mutableListOf()
    // чтобы нельзя было менять список снаружи, при получении значений из него будем создавать его копию с помощью. toMutableList()

    var coeff: List<Double>
        get() = _coeff.toList()
        protected set(value) {                  // можно измен только внутри класса и его потомков
            _coeff.clear()
            _coeff.addAll(value)
        }

    val power: Int
        get() = _coeff.size - 1

    init {                                        //копируем все элементы из параметра в свойство(нет ссылочной связи, записываем только копии)
        this._coeff.addAll(coeff)
        removeZeros()
    }
    // но массив не является элемнтом коллекции, создадим еще конструкторы
    constructor() : this(listOf(0.0))

    //constructor(c: MutableList<Double>) : this(c.toList())

    constructor(c: Array<Double>) : this(c.toList())

    constructor(vararg c: Double) : this(c.toList())

    // метод для проверки коррекности коэффицентов
    private fun removeZeros() {
        var found = false
        _coeff.indices.reversed().forEach {
            if (_coeff[it] neq 0.0) found = true
            else if (!found) _coeff.removeAt(it)
        }
        if (_coeff.size == 0) _coeff.add(0.0)
    }

    //вывод полинома в виде строки, i-й элемент списка - коэф при i-й степени
    override fun toString(): String =
    //мы должны выводить коэфф в обратном порядке
        //попробуем каждый элемент превратить в строку
        _coeff.indices.reversed()
            .joinToString(separator = "") { i ->    //it - парам по умолч переворачиваем индексы коэфф и для каждого из них..
                //чтобы не переписывать строку много раз используем спец класс для построения строк
                val mon = StringBuilder()
                if (_coeff[i] neq 0.0) {  //neq = !=
                    mon.append(if (_coeff[i] < 0) " - " else (if (i < _coeff.size - 1) " + " else ""))
                    val acoeff = abs(_coeff[i])
                    if (acoeff neq 1.0 || i == 0)    //проверка плохая
                        mon.append(acoeff)
                    if (i > 0) mon.append(variableName)
                    if (i > 1) mon.append("^$i")
                }
                if (mon.length == 0) mon.append(0)
                mon
            }

    operator fun invoke(x: Double): Double{  // теперь можно вызывать функцию, как р(2.0)
        var p = 1.0
        return _coeff.reduce { res, d -> p *= x; res + d * p } //объединяем элементы коллекции в общий результат res, d-знач каждого конкр коэф
        // нач знач res = 1 коэф, это быстрее, чем вычислять степени х
    }

    fun value(x: Double): Double{
        var p = 1.0
        return _coeff.reduce { res, d -> p *= x; res + d * p }
    }


    operator fun times(x: Double) =
        Polynom(_coeff.map {it * x})  // эта функция выполняет с каждым эл-том коллекции какое-то действие и возвращает измененную коллекцию

    operator fun timesAssign(x: Double){                   // теперь при присвоении мы не перезаписываем полином, а просто изменяем его значение
        _coeff.indices.forEach {_coeff[it] *= x}           // но теперь если р var, то возникает ошибка, тк комп не знает какой из опер исп, нужно будет исп оператор, как функцию
        removeZeros()
    }


    operator fun div(x: Double): Polynom =
        if (x eq 0.0) {
            Polynom(_coeff)
            //Exception("Нельзя делить на 0")
        }
        else {
            Polynom(_coeff.map { it / x })
        }

    operator fun divAssign(x: Double){
        if (x eq 0.0) {
            //throw Exception("Нельзя делить на 0")
        }
        else {
            _coeff.indices.forEach { _coeff[it] /= x }
            removeZeros()
        }
    }


    operator fun plus(other: Polynom) =
        Polynom(List(max(power, other.power) +1){
            if (it <= power) {_coeff[it]} else {0.0} +
                    (if (it <= other.power) {other._coeff[it]} else {0.0})
        })

    operator fun plusAssign(other: Polynom) {
        _coeff.indices.forEach {
            _coeff[it] += if (it <= other.power) other._coeff[it] else 0.0
        }
        if (power < other.power) {
            for (it in power + 1..other.power)
                _coeff.add(it, other._coeff[it])
        }
        removeZeros()
    }

    operator fun minus(other: Polynom) =
        Polynom(List(max(power, other.power) +1){
            if (it <= power) {_coeff[it]} else {0.0} -
                    (if (it <= other.power) {other._coeff[it]} else {0.0})
        })

    operator fun minusAssign(other: Polynom) {
        _coeff.indices.forEach {
            _coeff[it] -= if (it <= other.power) other._coeff[it] else 0.0
        }
        if (power < other.power) {
            for (it in power + 1..other.power)
                _coeff.add(it, (-1)*other._coeff[it])
        }
        removeZeros()
    }

    operator fun times(other: Polynom): Polynom {
        val product: MutableList<Double> = mutableListOf()
        _coeff.indices.forEach { i ->
            other._coeff.indices.forEach { j ->
                if (i+j > product.size-1) product.add(i+j, _coeff[i] * other._coeff[j])
                else product[i+j] += _coeff[i] * other._coeff[j]
            }
        }
        return Polynom(product)
    }

    operator fun timesAssign(other: Polynom) {
        coeff = (this * other)._coeff
    }

    override operator fun equals (other: Any?) =
        (other is Polynom) && (_coeff == other._coeff)   // умное  приведение типа

    override fun hashCode() = _coeff.hashCode()  //так как полиномы отлич только набором коэфф

    fun derivative(): Polynom{
        var c: MutableList<Double> = mutableListOf()
        for (i in 1..power){
            c.add(_coeff[i]*i)
        }
        return Polynom(c)
    }

}
operator fun Double.times(p: Polynom) = p * this

fun round10(x: Double): Double{
    var deg = 10.0.pow(10)
    return ((x * deg).roundToInt())/deg
}






