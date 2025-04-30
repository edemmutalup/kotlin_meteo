package kotlin_meteo

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import tornadofx.*

class UI : View("Погода") {

    override fun onUndock() {
        controller.cancelAllJobs()
        super.onUndock()
    }

    private val controller: WeatherController by inject()

    enum class Mode { CURRENT, RANGE }

    private val modeProperty = SimpleObjectProperty<Mode>(Mode.CURRENT)
    private var mode by modeProperty

    private val latField = textfield().apply {
        prefColumnCount = 8
        maxWidth = Region.USE_PREF_SIZE
        HBox.setHgrow(this, Priority.NEVER)
    }
    private val lonField = textfield().apply {
        prefColumnCount = 8
        maxWidth = Region.USE_PREF_SIZE
        HBox.setHgrow(this, Priority.NEVER)
    }
    private val fromPicker = datepicker().apply {
        prefWidth = 120.0
        maxWidth = Region.USE_PREF_SIZE
        HBox.setHgrow(this, Priority.NEVER)
    }
    private val toPicker = datepicker().apply {
        prefWidth = 120.0
        maxWidth = Region.USE_PREF_SIZE
        HBox.setHgrow(this, Priority.NEVER)
    }
    private val resultLabel = label()

    override val root = vbox(5.0) {
        prefWidth = 300.0

        style {
            fontFamily = "Verdana"
            fontSize = 12.px
        }

        val tg = ToggleGroup()
        hbox(1.0) {
            togglebutton("Текущая погода", tg) {
                isSelected = true
                action { mode = Mode.CURRENT }
            }
            togglebutton("Диапазон", tg) {
                action { mode = Mode.RANGE }
            }
        }

        form {
            fieldset("Координаты") {
                field("Широта")  { add(latField) }
                field("Долгота") { add(lonField) }
            }
        }

        form {
            visibleWhen { modeProperty.isEqualTo(Mode.RANGE) }
            managedWhen { modeProperty.isEqualTo(Mode.RANGE) }
            fieldset("Диапазон дат") {
                field("С")  { add(fromPicker) }
                field("По") { add(toPicker) }
            }
        }

        hbox {
            button("Сделать запрос") {
                hgrow = Priority.ALWAYS
                action { onAnalyzeClick() }
            }
        }
        add(resultLabel)
    }

    private fun onAnalyzeClick() {

        val lat = latField.text.toDoubleOrNull()
        val lon = lonField.text.toDoubleOrNull()
        if (lat == null || lon == null) {
            resultLabel.text = "Ошибка: координаты введены неверно"
            return
        }

        if (lat  !in  -90.0..90.0 || lon !in -180.0..180.0) {
            resultLabel.text = "Ошибка: координаты введены вне положенного диапазона"
            return
        }

        when (mode) {
            Mode.CURRENT -> {
                controller.loadCurrentWeather(lat, lon) { resultLabel.text = it }
            }
            Mode.RANGE -> {
                val from = fromPicker.value
                val to   = toPicker.value
                if (from == null || to == null) {
                    resultLabel.text = "Ошибка: выберите даты"
                    return
                }
                if (to.isBefore(from)) {
                    resultLabel.text = "Ошибка: дата окончания раньше начала"
                    return
                }
                controller.loadWeatherRange(lat, lon, from.toString(), to.toString()) { resultLabel.text = it }

            }
        }
    }
}