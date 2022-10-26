package ru.irlix.gusev.testovoe.screen.domain.util

import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// Утилитный класс, отображающий сколько раз вызвались методы bind у ViewHolder в вертикальном и горизонтальном итеме
object CounterBindUtil {

    private var verticalCount = AtomicInteger(0)
    private var horizontalCount = AtomicInteger(0)

    private var verticalListener: ((Int) -> Unit)? = null
    private var horizontalListener: ((Int) -> Unit)? = null

    fun addVertical() {
        GlobalScope.launch(Dispatchers.IO) {
            verticalCount.getAndIncrement()
            verticalListener?.invoke(verticalCount.get())
        }
    }

    fun addHorizontal() {
        GlobalScope.launch(Dispatchers.IO) {
            horizontalCount.getAndIncrement()
            horizontalListener?.invoke(horizontalCount.get())
        }
    }

    fun setVerticalListener(listener: (Int) -> Unit) {
        verticalListener = listener
    }

    fun setHorizontalListener(listener: (Int) -> Unit) {
        horizontalListener = listener
    }

    fun clear() {
        verticalListener = null
        horizontalListener = null
    }
}