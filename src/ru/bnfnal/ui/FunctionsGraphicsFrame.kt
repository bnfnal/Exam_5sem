package ru.bnfnal.ui

import ru.bnfnal.ui.painting.*
import ru.bnfnal.ui.polynoms.Newton
import ru.bnfnal.ui.polynoms.Polynom
import java.awt.Color
import java.awt.Dimension
import java.awt.event.*
import javax.swing.*
import kotlin.math.sin


class FunctionsGraphicsFrame: JFrame() {
    val minSz = Dimension(600, 600)

    var mainPanel: GraphicsPanel
    val controlPanel: JPanel

    var lbl_xMin: JLabel
    var lbl_xMax: JLabel
    var lbl_yMin: JLabel
    var lbl_yMax: JLabel
    var lbl_tMin: JLabel
    var lbl_tMax: JLabel

    var cb_explicit_function: JCheckBox
    var cb_parameterized_function: JCheckBox

    var panel_Color_explicit_function_graph: JPanel
    var panel_Color_parameterized_function_graph: JPanel

    var xMinM: SpinnerNumberModel
    var xMin: JSpinner

    var xMaxM: SpinnerNumberModel
    var xMax: JSpinner

    var yMinM: SpinnerNumberModel
    var yMin: JSpinner

    var yMaxM: SpinnerNumberModel
    var yMax: JSpinner

    var tMinM: SpinnerNumberModel
    var tMin: JSpinner

    var tMaxM: SpinnerNumberModel
    var tMax: JSpinner

    init {
        minimumSize = minSz
        defaultCloseOperation = EXIT_ON_CLOSE

        setTitle("Построение графиков функций, заданных явно и параметрически")

        xMinM = SpinnerNumberModel(-5.0, -1000.0, 4.9, 0.1)
        xMin = JSpinner(xMinM)

        xMaxM = SpinnerNumberModel(5.0, -4.9, 1000.0, 0.1)
        xMax = JSpinner(xMaxM)

        yMinM = SpinnerNumberModel(-5.0, -1000.0, 4.9, 0.1)
        yMin = JSpinner(yMinM)

        yMaxM = SpinnerNumberModel(5.0, -4.9, 1000.0, 0.1)
        yMax = JSpinner(yMaxM)

        tMinM = SpinnerNumberModel(-5.0, -1000.0, 4.9, 0.1)
        tMin = JSpinner(tMinM)

        tMaxM = SpinnerNumberModel(5.0, -4.9, 1000.0, 0.1)
        tMax = JSpinner(tMaxM)

        var explicit_function: (Double) -> Double = { it -> if (it >= 0) 1 / it else -1 / it }
        var x_parameterized_function: (Double) -> Double = { it -> sin(2 * it) }
        var y_parameterized_function: (Double) -> Double = { it -> sin(4 * it) }

        val plane = CartesianPlane(
            xMin.value as Double,
            xMax.value as Double,
            yMin.value as Double,
            yMax.value as Double,
            tMin.value as Double,
            tMax.value as Double,
            x_parameterized_function,
            y_parameterized_function
        )

        xMin.value = plane.xMin
        xMax.value = plane.xMax
        yMin.value = plane.yMin
        yMax.value = plane.yMax

        val cartesianPainter = CartesianPainter(plane)

        var explicit_function_graphPainter = ExplicitFunctionPainter(plane, explicit_function)
        explicit_function_graphPainter.funColor = Color.BLUE

        var parameterized_function_graphPainter = ParameterizedFunctionPainter(plane, x_parameterized_function, y_parameterized_function)
        parameterized_function_graphPainter.funColor = Color.PINK

        var painters = mutableListOf(cartesianPainter, explicit_function_graphPainter, parameterized_function_graphPainter)

        mainPanel = GraphicsPanel(painters).apply {
            background = Color.WHITE
        }

        mainPanel.addComponentListener(object : ComponentAdapter() {
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

        cb_explicit_function = JCheckBox("функция, заданная явно", true)
        cb_parameterized_function = JCheckBox("функция, заданная параметрически", true)

        cb_explicit_function.addItemListener(object : ItemListener {
            override fun itemStateChanged(e: ItemEvent?) {
                if (e?.stateChange == 1) painters.add(explicit_function_graphPainter)
                else painters.remove(explicit_function_graphPainter)
                mainPanel.repaint()
            }
        })

        cb_parameterized_function.addItemListener(object : ItemListener {
            override fun itemStateChanged(e: ItemEvent?) {
                if (e?.stateChange == 1) painters.add(parameterized_function_graphPainter)
                else painters.remove(parameterized_function_graphPainter)
                mainPanel.repaint()
            }
        })

        panel_Color_explicit_function_graph = JPanel().apply {
            background = Color.blue
            minimumSize = Dimension(60, 60)
        }
        panel_Color_parameterized_function_graph = JPanel().apply {
            background = Color.pink
            minimumSize = Dimension(60, 60)
        }

        panel_Color_explicit_function_graph.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e?.button == 1) {
                    val color =
                        JColorChooser.showDialog(null, "Выберите цвет", panel_Color_explicit_function_graph.background)
                    panel_Color_explicit_function_graph.background = color
                    explicit_function_graphPainter.funColor = color
                    mainPanel.repaint()
                }
            }
        })

        panel_Color_parameterized_function_graph.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e?.button == 1) {
                    val color = JColorChooser.showDialog(
                        null,
                        "Выберите цвет",
                        panel_Color_parameterized_function_graph.background
                    )
                    panel_Color_parameterized_function_graph.background = color
//                    parameterized_function_graphPainter.funColor = color
                    mainPanel.repaint()
                }
            }
        })

        xMax.addChangeListener {
            xMinM.maximum = xMax.value as Double - 0.1
            plane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        xMin.addChangeListener {
            xMaxM.minimum = xMin.value as Double + 0.1
            plane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        yMax.addChangeListener {
            yMinM.maximum = yMax.value as Double - 0.1
            plane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
            mainPanel.repaint()
        }
        yMin.addChangeListener {
            yMaxM.minimum = yMin.value as Double + 0.1
            plane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
            mainPanel.repaint()
        }
        tMax.addChangeListener {
            tMinM.maximum = tMax.value as Double - 0.1
            plane.tSegment = Pair(tMin.value as Double, tMax.value as Double)
            mainPanel.repaint()
        }
        tMin.addChangeListener {
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
                            .addComponent(
                                mainPanel,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE
                            )
                            .addComponent(
                                controlPanel,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE
                            )
                    )
                    .addGap(4)
            )

            setVerticalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addComponent(
                        mainPanel,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE
                    )
                    .addGap(4)
                    .addComponent(
                        controlPanel,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.PREFERRED_SIZE
                    )
                    .addGap(4)
            )
        }

        controlPanel.layout = GroupLayout(controlPanel).apply {

            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(
                                lbl_xMin,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addComponent(
                                lbl_yMin,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addComponent(
                                lbl_tMin,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
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
                            .addComponent(
                                lbl_xMax,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addComponent(
                                lbl_yMax,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addComponent(
                                lbl_tMax,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                    )
                    .addGap(2)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(xMax, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                            .addComponent(yMax, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                            .addComponent(tMax, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                    )
                    .addGap(8, 8, Int.MAX_VALUE)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(
                                cb_explicit_function,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addComponent(
                                cb_parameterized_function,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                    )
                    .addGap(4)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(panel_Color_explicit_function_graph, 20, 20, GroupLayout.PREFERRED_SIZE)
                            .addComponent(panel_Color_parameterized_function_graph, 20, 20, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGap(4)
            )

            setVerticalGroup(
                createParallelGroup()
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                lbl_xMin,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                lbl_yMin,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                lbl_tMin,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                xMin,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                yMin,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                tMin,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)

                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                lbl_xMax,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                lbl_yMax,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                lbl_tMax,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                xMax,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                yMax,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                tMax,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(
                                cb_explicit_function,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8)
                            .addComponent(
                                cb_parameterized_function,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(panel_Color_explicit_function_graph, 20, 20, GroupLayout.PREFERRED_SIZE)
                            .addGap(8)
                            .addComponent(panel_Color_parameterized_function_graph, 20, 20, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                    )
            )

            linkSize(panel_Color_explicit_function_graph, panel_Color_parameterized_function_graph)
            linkSize(cb_explicit_function, cb_parameterized_function)
            linkSize(xMin, xMax, yMin, yMax, tMin, tMax)
            linkSize(lbl_xMin, lbl_xMax, lbl_yMin, lbl_yMin, lbl_tMin, lbl_tMax)

        }
        pack()
        plane.width = mainPanel.width
        plane.height = mainPanel.height
    }
}