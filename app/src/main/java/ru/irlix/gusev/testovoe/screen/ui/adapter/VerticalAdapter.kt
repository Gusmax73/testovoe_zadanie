package ru.irlix.gusev.testovoe.screen.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.irlix.gusev.testovoe.R
import ru.irlix.gusev.testovoe.databinding.ItemVerticalBinding
import ru.irlix.gusev.testovoe.screen.domain.util.CounterBindUtil
import ru.irlix.gusev.testovoe.screen.ui.model.VerticalModel

class VerticalAdapter : RecyclerView.Adapter<VerticalAdapter.VerticalViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val verticalItems = mutableListOf<VerticalModel>()
    private val recyclerViewPool = RecyclerView.RecycledViewPool().apply {
        setMaxRecycledViews(R.layout.item_horizontal, 100)
    }

    override fun getItemId(position: Int): Long {
        return verticalItems[position].lineNumber.toLong()
    }

    override fun getItemCount(): Int = verticalItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalViewHolder {
        val viewBinding = ItemVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        viewBinding.rvHorizontal.setRecycledViewPool(recyclerViewPool)
        viewBinding.rvHorizontal.setHasFixedSize(true)

        return VerticalViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: VerticalViewHolder, position: Int) {
        verticalItems[position].let {
            holder.bind(it)
        }
    }

    fun setData(items: List<VerticalModel>) {
        verticalItems.clear()
        verticalItems.addAll(items)
    }

    inner class VerticalViewHolder(
        private val viewBinding: ItemVerticalBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        private val horizontalAdapter by lazy { HorizontalAdapter() }

        fun bind(model: VerticalModel) {
            model.callbackChangeNumber = horizontalAdapter::updateItem
            CounterBindUtil.addVertical()

            viewBinding.tvLineNumber.text = model.lineNumber.toString()
            viewBinding.rvHorizontal.adapter = horizontalAdapter

            horizontalAdapter.setItems(model.horizontalItems)
        }
    }
}