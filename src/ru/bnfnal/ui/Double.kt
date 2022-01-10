package ru.bnfnal

import kotlin.Double
import kotlin.math.max

infix fun Double. eq(other: Double) =
    Math.abs(this - other) < Math.max(Math.ulp(this), Math.ulp(other)) *2

infix fun Double. neq(other: Double) =
    Math.abs(this - other) > Math.max(Math.ulp(this), Math.ulp(other)) *2

infix fun Double. ge(other: Double) =
    this > other || this.eq(other)

infix fun Double. le(other: Double) =
    this < other || this.eq(other)