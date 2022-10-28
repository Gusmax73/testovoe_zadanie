package ru.irlix.gusev.testovoe.screen.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.irlix.gusev.testovoe.screen.ui.adapter.VerticalDiffCallback
import ru.irlix.gusev.testovoe.screen.ui.model.HorizontalModel
import ru.irlix.gusev.testovoe.screen.ui.model.VerticalModel
import kotlin.random.Random

class MainViewModel : ViewModel() {

    companion object {
        private const val COUNT_ITEMS_VERTICAL = 100
        private const val COUNT_ITEMS_HORIZONTAL = 10
        private const val NUMBER_MAX_VALUE = 99999
        private const val DELAY = 1000L
    }

    private var previousRandomPosition = -1
    private val verticalItems = mutableListOf<VerticalModel>()

    private val _diffResultItems = MutableLiveData<Pair<List<VerticalModel>, DiffUtil.DiffResult>>()
    val diffResultItems: LiveData<Pair<List<VerticalModel>, DiffUtil.DiffResult>> = _diffResultItems

    var firstVisiblePosition = 0
    var lastVisiblePosition = 0

    fun init() {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch(Dispatchers.IO) {
            setItems(
                newItems = getAllItems()
            )
            while (true) {
                delay(DELAY)
                setItems(
                    newItems = getChangedItems()
                )
            }
        }
    }

    private fun setItems(newItems: List<VerticalModel>) {
        val diffCallback = VerticalDiffCallback(verticalItems, newItems)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)

        verticalItems.clear()
        verticalItems.addAll(newItems)

        _diffResultItems.postValue(verticalItems to diffResult)
    }

    private fun getChangedItems(): List<VerticalModel> {
        val newItems = ArrayList<VerticalModel>(COUNT_ITEMS_VERTICAL)

        val currentPosition = getRandomPosition()
        val newPosition = getRandomPosition()

        val bufferItem = verticalItems[currentPosition]

        for (index in 0 until verticalItems.size) {
            val verticalModel = verticalItems[index]
            changeOneNumberInHorizontal(index, verticalModel)

            when (index) {
                currentPosition -> {
                    // nothing
                }
                newPosition -> {
                    newItems.add(bufferItem)
                    newItems.add(verticalModel)
                }
                else -> {
                    newItems.add(verticalModel)
                }
            }
        }

        return newItems
    }

    private fun changeOneNumberInHorizontal(indexVertical: Int, verticalModel: VerticalModel) {
        val indexChange = Random.nextInt(0, COUNT_ITEMS_HORIZONTAL - 1)
        var newNumber = Random.nextInt(0, NUMBER_MAX_VALUE)
        val oldNumber = verticalModel.horizontalItems[indexChange].number

        while (newNumber == oldNumber) {
            newNumber = Random.nextInt(0, NUMBER_MAX_VALUE)
        }

        val isRedraw = indexVertical in firstVisiblePosition..lastVisiblePosition
        verticalModel.callbackChangeNumber?.invoke(indexChange, newNumber, isRedraw)
    }

    // Исключает выдачу повторных подряд позиций, чтобы гарантированно перемещать строку
    private fun getRandomPosition(): Int {
        var newPosition = Random.nextInt(0, COUNT_ITEMS_VERTICAL - 1)

        while (previousRandomPosition == newPosition) {
            newPosition = Random.nextInt(0, COUNT_ITEMS_VERTICAL - 1)
        }
        previousRandomPosition = newPosition

        return newPosition
    }

    private fun getAllItems(): List<VerticalModel> {
        val newItems = mutableListOf<VerticalModel>()

        for (indexVertical in 0 until COUNT_ITEMS_VERTICAL) {
            val horizontalItems = mutableListOf<HorizontalModel>()

            for (indexHorizontal in 0 until COUNT_ITEMS_HORIZONTAL) {
                val newNumber = Random.nextInt(0, NUMBER_MAX_VALUE)
                val horizontalModel = HorizontalModel(
                    id = indexHorizontal.toLong(),
                    number = newNumber
                )
                horizontalItems.add(horizontalModel)
            }

            val lineNumber = indexVertical + 1
            val verticalModel = VerticalModel(
                lineNumber = lineNumber,
                horizontalItems = horizontalItems
            )
            newItems.add(verticalModel)
        }

        return newItems
    }
}