package ru.irlix.gusev.testovoe.screen.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import ru.irlix.gusev.testovoe.R
import ru.irlix.gusev.testovoe.databinding.FragmentMainBinding
import ru.irlix.gusev.testovoe.screen.domain.util.CounterBindUtil
import ru.irlix.gusev.testovoe.screen.ui.adapter.VerticalAdapter
import ru.irlix.gusev.testovoe.screen.ui.model.VerticalModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding

    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private val adapter by lazy { VerticalAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvVertical) {
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            recycledViewPool.setMaxRecycledViews(R.layout.item_vertical, 20)
            adapter = this@MainFragment.adapter
        }

        viewModel.init()

        viewModel.diffResultItems.observe(viewLifecycleOwner) { (items, diffResult) ->
            updateItems(items, diffResult)
        }
    }

    override fun onStart() {
        super.onStart()
        CounterBindUtil.setHorizontalListener(::setHorizontalCountNumber)
        CounterBindUtil.setVerticalListener(::setVerticalCountNumber)
    }

    override fun onStop() {
        super.onStop()
        CounterBindUtil.clear()
    }

    private fun setVerticalCountNumber(count: Int) {
        Handler(Looper.getMainLooper()).post {
            binding.tvCounterNumberVertical.text = count.toString()
        }
    }

    private fun setHorizontalCountNumber(count: Int) {
        Handler(Looper.getMainLooper()).post {
            binding.tvCounterNumberHorizontal.text = count.toString()
        }
    }

    private fun updateItems(items: List<VerticalModel>, diffResult: DiffUtil.DiffResult) {
        adapter.setData(items)
        diffResult.dispatchUpdatesTo(adapter)
    }
}