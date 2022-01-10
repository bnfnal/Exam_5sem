package ru.bnfnal.ui


import ru.bnfnal.ui.painting.Painter
import java.awt.Graphics
import javax.swing.JPanel

class GraphicsPanel(val painters: List<Painter>): JPanel(){
//class GraphicsPanel(vararg val painters: Painter): JPanel(){
    override  fun paint(g: Graphics?){
        super.paint(g)      // супер - ключ слово, обознач базовый класс (родитель) (как this)
        g?.let{
            painters.forEach { p->
                p.paint(it)
            }
        }
    }
}