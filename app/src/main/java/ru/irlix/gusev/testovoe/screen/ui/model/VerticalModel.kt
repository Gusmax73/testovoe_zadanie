package ru.irlix.gusev.testovoe.screen.ui.model

data class VerticalModel(
    val lineNumber: Int,
    val horizontalItems: List<HorizontalModel>,
    var callbackChangeNumber: ((Int, Int) -> Unit)? = null
)