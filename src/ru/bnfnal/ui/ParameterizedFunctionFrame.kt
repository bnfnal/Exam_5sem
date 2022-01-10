package ru.bnfnal.ui

import ru.bnfnal.ui.painting.*
import java.awt.Color
import java.awt.Dimension
import java.awt.event.*
import javax.swing.*
import kotlin.math.sin


class ParameterizedFunctionFrame: JFrame() {
    val minSz = Dimension(600, 600)

    var mainPanel: GraphicsPanel
    val controlPanel: JPanel

    var lbl_xMin: JLabel
    var lbl_xMax: JLabel
    var lbl_yMin: JLabel
    var lbl_yMax: JLabel
    var lbl_tMin: JLabel
    var lbl_tMax: JLabel

    var lbl_Color_parametrize_function: JLabel
    var panel_Color_parameterized_function_graph: JPanel

    var xMin: JTextField
    var xMax: JTextField
    var yMin: JTextField
    var yMax: JTextField

    var tMinM: SpinnerNumberModel
    var tMin: JSpinner

    var tMaxM: SpinnerNumberModel
    var tMax: JSpinner

    init {
        minimumSize = minSz

        //defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null);

        setTitle("Построение графика параметрически заданной функции")

        tMinM = SpinnerNumberModel(-100.0, -10000.0, 99.9, 0.1)
        tMin = JSpinner(tMinM)

        tMaxM = SpinnerNumberModel(100.0, -99.9, 10000.0, 0.1)
        tMax = JSpinner(tMaxM)

        var x_parameterized_function: (Double) -> Double = { it -> sin(2 * it) }
        var y_parameterized_function: (Double) -> Double = { it -> sin(4 * it) }

        val plane = CartesianPlane(
            tMin.value as Double,
            tMax.value as Double,
            x_parameterized_function,
            y_parameterized_function
        )

        xMin = JTextField(plane.xMin.toString())
        xMax = JTextField(plane.xMax.toString())
        yMin = JTextField(plane.xMin.toString())
        yMax = JTextField(plane.xMin.toString())

        println(plane.xFunction.toString())
        println(plane.yFunction.toString())
        println(plane.tMin)
        println(plane.tMax)

        val cartesianPainter = CartesianPainter(plane)

        var parameterized_function_graphPainter = ParameterizedFunctionPainter(plane, x_parameterized_function, y_parameterized_function)
        parameterized_function_graphPainter.funColor = Color.PINK

        var painters = mutableListOf(cartesianPainter, parameterized_function_graphPainter)

        mainPanel = GraphicsPanel(painters).apply {
            background = Color.WHITE
        }

        mainPanel.addComponentListener(object : ComponentAdapter(){
            override fun componentResized(e: ComponentEvent?) {
                plane.width = mainPanel.width
                plane.height = mainPanel.height
                mainPanel.repaint()
            }
        })

        controlPanel = JPanel()

        lbl_xMin = JLabel("Xmin:")
        lbl_xMax = JLabel("Xmax:")
        lbl_yMin = JLabel("Ymin:")
        lbl_yMax = JLabel("Ymax:")
        lbl_tMin = JLabel("Tmin:")
        lbl_tMax = JLabel("Tmax:")


        lbl_Color_parametrize_function = JLabel("цвет функции")

        panel_Color_parameterized_function_graph = JPanel().apply {
            background = Color.PINK
            minimumSize = Dimension(60, 60)
        }

        panel_Color_parameterized_function_graph.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                if (e?.button == 1) {
                    val color = JColorChooser.showDialog(null, "Выберите цвет", panel_Color_parameterized_function_graph.background)
                    panel_Color_parameterized_function_graph.background = color
                    parameterized_function_graphPainter.funColor = color
                    mainPanel.repaint()
                }
            }
        })

        tMax.addChangeListener{
            tMinM.maximum = tMax.value as Double - 0.1
            plane.tSegment = Pair(tMin.value as Double, tMax.value as Double)
            mainPanel.repaint()
        }
        tMin.addChangeListener{
            tMaxM.minimum = tMin.value as Double + 0.1
            plane.tSegment = Pair(tMin.value as Double, tMax.value as Double)
            mainPanel.repaint()
        }

        layout = GroupLayout(contentPane).apply {

            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                            .addComponent(controlPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    )
                    .addGap(4)
            )

            setVerticalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addGap(4)
                    .addComponent(controlPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(4)
            )
        }

        controlPanel.layout = GroupLayout(controlPanel).apply {

            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(lbl_xMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_yMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_tMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGap(2)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(xMin, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                            .addComponent(yMin, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                            .addComponent(tMin, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                    )
                    .addGap(8, 8, Int.MAX_VALUE)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(lbl_xMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_yMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_tMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGap(2)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(xMax, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                            .addComponent(yMax, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                            .addComponent(tMax, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                    )
                    .addGap(8, 8, Int.MAX_VALUE)
                    .addComponent(lbl_Color_parametrize_function, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(4)
                    .addComponent(panel_Color_parameterized_function_graph, 20, 20, GroupLayout.PREFERRED_SIZE)
                    .addGap(4)
            )

            setVerticalGroup(
                createParallelGroup()
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(lbl_xMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(lbl_yMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(lbl_tMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(xMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(yMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(tMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)

                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(lbl_xMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(lbl_yMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(lbl_tMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(xMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(yMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(tMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addGroup(
                                createParallelGroup()
                                    .addComponent(lbl_Color_parametrize_function, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel_Color_parameterized_function_graph, 20, 20, GroupLayout.PREFERRED_SIZE)
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                    )
            )

            linkSize(xMin, xMax, yMin, yMax, tMin, tMax)
            linkSize(lbl_xMin, lbl_xMax, lbl_yMin, lbl_yMin, lbl_tMin, lbl_tMax)

        }
        pack()
        plane.width = mainPanel.width
        plane.height = mainPanel.height
    }
}