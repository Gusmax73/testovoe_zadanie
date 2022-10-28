package ru.irlix.gusev.testovoe.screen.ui.adapter

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.irlix.gusev.testovoe.R
import ru.irlix.gusev.testovoe.databinding.ItemHorizontalBinding
import ru.irlix.gusev.testovoe.screen.domain.util.CounterBindUtil
import ru.irlix.gusev.testovoe.screen.ui.model.HorizontalModel

class HorizontalAdapter : RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder>() {

    private val horizontalItems = mutableListOf<HorizontalModel>()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return horizontalItems[position].id
    }

    override fun getItemCount(): Int = horizontalItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {
        val viewBinding = ItemHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HorizontalViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {
        horizontalItems[position].let {
            holder.bind(it)
        }
    }

    fun setItems(horizontals: List<HorizontalModel>) {
        horizontalItems.clear()
        horizontalItems.addAll(horizontals)
        notifyDataSetChanged()
    }

    // isRedraw - нужен, чтобы после пролистывания вертикального списка,
    // в горизонтальном списке не начали обновляться по несколько чисел за один раз
    // (т.е. если убрать проверку, то проскроллив вниз, будут обновляться несколько айтемов в 1 секунду)
    fun updateItem(position: Int, newNumber: Int, isRedraw: Boolean) {
        horizontalItems[position].number = newNumber
        if (isRedraw) {
            Handler(Looper.getMainLooper()).post {
                notifyItemChanged(position)
            }
        }
    }

    inner class HorizontalViewHolder(
        private val viewBinding: ItemHorizontalBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(model: HorizontalModel) {
            CounterBindUtil.addHorizontal()
            viewBinding.tvNumber.text = model.number.toString()

            showRedraw(viewBinding)
        }

        private fun showRedraw(viewBinding: ItemHorizontalBinding) {
            val colorFrom: Int = viewBinding.root.context.getColor(R.color.red)
            val colorTo: Int = viewBinding.root.context.getColor(R.color.white)
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            colorAnimation.duration = 700 // milliseconds

            colorAnimation.addUpdateListener { animator -> viewBinding.tvNumber.setBackgroundColor(animator.animatedValue as Int) }
            colorAnimation.start()
        }
    }
}