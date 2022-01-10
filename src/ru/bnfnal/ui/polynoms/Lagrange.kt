package ru.bnfnal.ui.polynoms

class Lagrange(private val points: Map<Double, Double>): Polynom() {  // в карту нельзя записать значения с одинак ключами => узлы не будут повторяться

    init{
        coeff = Polynom().apply {
            points.keys.forEach {
                this += fundamental(it) * points[it]!!      // эл-та с этим ключом может не сущ-ть
            }
        }.coeff
    }

    fun fundamental(x: Double) = Polynom(1.0).apply {
        points.keys.forEach {
            if (it != x) this *= Polynom(-it, 1.0) / (x - it)
        }
    }
}
