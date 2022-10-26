package ru.irlix.gusev.testovoe.screen.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.irlix.gusev.testovoe.screen.ui.model.VerticalModel

class VerticalDiffCallback(
    private val oldItems: List<VerticalModel>,
    private val newItems: List<VerticalModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].lineNumber == newItems[newItemPosition].lineNumber
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }
}