package ru.bnfnal.ui

import ru.bnfnal.ui.painting.*
import java.awt.Color
import java.awt.Dimension
import java.awt.event.*
import javax.swing.*


class ExplicitFunctionFrame: JFrame() {
    val minSz = Dimension(600, 600)

    var mainPanel: GraphicsPanel
    val controlPanel: JPanel

    var lbl_xMin: JLabel
    var lbl_xMax: JLabel
    var lbl_yMin: JLabel
    var lbl_yMax: JLabel

    var lbl_Color_explicit_function: JLabel
    var panel_Color_explicit_function_graph: JPanel

    var xMinM: SpinnerNumberModel
    var xMin: JSpinner

    var xMaxM: SpinnerNumberModel
    var xMax: JSpinner

    var yMinM: SpinnerNumberModel
    var yMin: JSpinner

    var yMaxM: SpinnerNumberModel
    var yMax: JSpinner

    init {
        minimumSize = minSz

        //defaultCloseOperation = EXIT_ON_CLOSE

        setTitle("Построение графика явно заданой функции")

        xMinM = SpinnerNumberModel(-10.0, -1000.0, 9.9, 0.1)
        xMin = JSpinner(xMinM)

        xMaxM = SpinnerNumberModel(10.0, -9.9, 1000.0, 0.1)
        xMax = JSpinner(xMaxM)

        yMinM = SpinnerNumberModel(-10.0, -1000.0, 9.9, 0.1)
        yMin = JSpinner(yMinM)

        yMaxM = SpinnerNumberModel(10.0, -9.9, 1000.0, 0.1)
        yMax = JSpinner(yMaxM)

        val plane = CartesianPlane(
            xMin.value as Double,
            xMax.value as Double,
            yMin.value as Double,
            yMax.value as Double
        )

        val cartesianPainter = CartesianPainter(plane)
        val pointsPainter = PointsPainter(plane)
        var explicit_function: (Double) -> Double = { it -> if (it>=0) 1/it else -1/it }

        var explicit_function_graphPainter = ExplicitFunctionPainter(plane, explicit_function)
        explicit_function_graphPainter.funColor = Color.BLUE

        var painters = mutableListOf(cartesianPainter, explicit_function_graphPainter)

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

        lbl_Color_explicit_function = JLabel("цвет функции")

        panel_Color_explicit_function_graph = JPanel().apply {
            background = Color.blue
            minimumSize = Dimension(60, 60)
        }

        panel_Color_explicit_function_graph.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                if (e?.button == 1) {
                    val color = JColorChooser.showDialog(null, "Выберите цвет", panel_Color_explicit_function_graph.background)
                    panel_Color_explicit_function_graph.background = color
                    explicit_function_graphPainter.funColor = color
                    mainPanel.repaint()
                }
            }
        })

        xMax.addChangeListener{
            xMinM.maximum = xMax.value as Double - 0.1
            plane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        xMin.addChangeListener{
            xMaxM.minimum = xMin.value as Double + 0.1
            plane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        yMax.addChangeListener{
            yMinM.maximum = yMax.value as Double - 0.1
            plane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
            mainPanel.repaint()
        }
        yMin.addChangeListener{
            yMaxM.minimum = yMin.value as Double + 0.1
            plane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
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
                    )
                    .addGap(2)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(xMin, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                            .addComponent(yMin, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                    )
                    .addGap(8, 8, Int.MAX_VALUE)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(lbl_xMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_yMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGap(2)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(xMax, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                            .addComponent(yMax, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.DEFAULT_SIZE)
                    )
                    .addGap(8, 8, Int.MAX_VALUE)
                    .addComponent(lbl_Color_explicit_function, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(4)
                    .addComponent(panel_Color_explicit_function_graph, 20, 20, GroupLayout.PREFERRED_SIZE)
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
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(xMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(yMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)

                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(lbl_xMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(lbl_yMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(xMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addComponent(yMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, Int.MAX_VALUE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(8, 8, Int.MAX_VALUE)
                            .addGroup(
                                createParallelGroup()
                                    .addComponent(lbl_Color_explicit_function, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel_Color_explicit_function_graph, 20, 20, GroupLayout.PREFERRED_SIZE)
                            )
                            .addGap(8, 8, Int.MAX_VALUE)
                    )


            )

            linkSize(xMin, xMax, yMin, yMax)
            linkSize(lbl_xMin, lbl_xMax, lbl_yMin, lbl_yMin)

        }
        pack()
        plane.width = mainPanel.width
        plane.height = mainPanel.height
    }
}