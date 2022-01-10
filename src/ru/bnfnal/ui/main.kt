package ru.bnfnal.ui

import ru.bnfnal.ui.painting.ParameterizedFunctionPainter

fun main() {
    val wnd1 = ExplicitFunctionFrame()
    val wnd2 = ParameterizedFunctionFrame()
    wnd1.isVisible = true
    wnd2.isVisible = true
}