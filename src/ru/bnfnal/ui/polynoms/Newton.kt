package ru.bnfnal.ui.polynoms

import kotlin.math.pow
import kotlin.math.roundToInt

class Newton(private val points: Map<Double, Double>): Polynom() {   // в карту нельзя записать значения с одинак ключами => узлы не будут повторяться

    private val n = points.size-1
    private val om = Polynom(1.0)

    public val x: MutableList<Double> = mutableListOf()
    public val y: MutableList<Double> = mutableListOf()


    init{
        points.keys.forEach {
            x.add(it)
            y.add(points[it] !!)
        }

        coeff = Polynom().apply {
            this += Polynom(y[0])
            var o =0.0
            var t: Int
            for (k in 1..n) {
                this += omega(k) * difference(k)
            }
        }.coeff
    }

    fun difference( k: Int): Double {
        var p = 1.0
        var s = 0.0
        for (i in 0..k) {
            for (j in 0..k) {
                if (j != i) p *= (x[i] - x[j])
            }
            s += y[i] / p
            p = 1.0
        }
        return s
    }


    fun omega( k: Int) = om.apply {
        this *= Polynom(-x[k-1], 1.0)
    }

    fun add(a: Double, b: Double) {
        if (a in x)
        {
            if (b in y)
            else {
                //throw Exception("Нельзя добавлять")
            }
        }
        else {
            x.add(a)
            y.add(b)
            this += (omega(n+1) * difference(n+1))
        }
    }
}
