package kotlin_meteo

import javafx.stage.Stage
import tornadofx.*




class MyApp : App(UI::class) {
    override fun start(stage: Stage) {
        with(stage) {
            width = 400.0
            height = 480.0
            minWidth = width
            minHeight = height
            isResizable = true
        }
        super.start(stage)
    }
}

fun main(args: Array<String>): Unit {
    launch<MyApp>(args)
}
