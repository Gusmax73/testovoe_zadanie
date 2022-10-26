package ru.irlix.gusev.testovoe.screen.ui.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

    fun updateItem(position: Int, newNumber: Int) {
        horizontalItems[position].number = newNumber
        Handler(Looper.getMainLooper()).post {
            notifyItemChanged(position)
        }
    }

    inner class HorizontalViewHolder(
        private val viewBinding: ItemHorizontalBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(model: HorizontalModel) {
            CounterBindUtil.addHorizontal()
            viewBinding.tvNumber.text = model.number.toString()

//            val colorFrom: Int = itemView.context.getColor(R.color.red)
//            val colorTo: Int = itemView.context.getColor(R.color.white)
//            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
//            colorAnimation.duration = 700 // milliseconds
//
//            colorAnimation.addUpdateListener { animator -> viewBinding.tvNumber.setBackgroundColor(animator.animatedValue as Int) }
//            colorAnimation.start()
        }
    }
}