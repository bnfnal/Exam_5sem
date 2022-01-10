package ru.bnfnal.ui

import ru.bnfnal.ui.painting.ParameterizedFunctionPainter

fun main() {
    val wnd = MainFrame()
    val wnd1 = ExplicitFunctionFrame()
    val wnd2 = ParameterizedFunctionFrame()
    wnd.isVisible = true         //при закрытии окна работа приложения не заканчивается
    wnd1.isVisible = true
    wnd2.isVisible = true
}