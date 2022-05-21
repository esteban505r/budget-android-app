package com.esteban.lopez.budget.view.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esteban.lopez.budget.Application
import com.esteban.lopez.budget.R
import com.esteban.lopez.budget.databinding.FragmentListValuesBinding
import com.esteban.lopez.budget.model.db.entities.Expense
import com.esteban.lopez.budget.model.db.entities.Income
import com.esteban.lopez.budget.model.db.entities.Value
import com.esteban.lopez.budget.model.db.entities.ValueAndCategory
import com.esteban.lopez.budget.view.adapters.ValuesAdapter
import com.esteban.lopez.budget.viewmodel.ExpenseViewModel
import com.esteban.lopez.budget.viewmodel.ExpenseViewModelFactory
import com.esteban.lopez.budget.viewmodel.IncomeViewModel
import com.esteban.lopez.budget.viewmodel.IncomeViewModelFactory
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ExpensesFragment : Fragment() {

    lateinit var binding: FragmentListValuesBinding


    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((activity?.application as Application).incomeRepository)
    }

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((activity?.application as Application).expenseRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListValuesBinding.inflate(layoutInflater)
        val view = binding.root;
        binding.textView6.setText(R.string.expenses)

        setUpList()

        lifecycleScope.launchWhenResumed {
            expenseViewModel.getExpensesAndCategory().collect {
                val values = mutableListOf<ValueAndCategory>()
                values.addAll(it)
                values.sortWith { element, element2 ->
                    if (element.value.date > element2.value.date) 0 else 1
                }
                (binding.valuesRecyclerView.adapter as ValuesAdapter).changeData(values,true)
                if(values.isEmpty()){
                    binding.textView8.visibility = View.VISIBLE
                    binding.imageView3.visibility = View.VISIBLE
                    binding.valuesRecyclerView.visibility = View.GONE
                }
                else{
                    binding.textView8.visibility = View.GONE
                    binding.imageView3.visibility = View.GONE
                    binding.valuesRecyclerView.visibility = View.VISIBLE
                }
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("success")?.observe(viewLifecycleOwner) {result ->
            if(result){
                Snackbar.make(binding.root,R.string.value_updated,Snackbar.LENGTH_SHORT).show()
            }
            else{
                Snackbar.make(binding.root,R.string.error_value_updated,Snackbar.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun setUpList() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftLabel(getString(R.string.delete))
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.holo_red_light
                        )
                    )
                    .setSwipeLeftLabelColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.white
                        )
                    )
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if ((viewHolder as ValuesAdapter.ViewHolder).type == 0) {
                    if (viewHolder.itemId != 0.toLong()) {
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                val deleted = incomeViewModel.delete(
                                    (binding.valuesRecyclerView.adapter as ValuesAdapter).getValue(
                                        viewHolder.bindingAdapterPosition
                                    ) as Income
                                )
                                withContext(Dispatchers.Main) {
                                    if (deleted > 0) {
                                        (binding.valuesRecyclerView.adapter as ValuesAdapter?)?.deleteItem(
                                            viewHolder.bindingAdapterPosition,
                                            true
                                        )
                                    } else {
                                        (binding.valuesRecyclerView.adapter as ValuesAdapter?)?.notifyItemChanged(
                                            viewHolder.bindingAdapterPosition
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        (binding.valuesRecyclerView.adapter as ValuesAdapter).notifyItemChanged(
                            viewHolder.bindingAdapterPosition
                        )
                    }
                } else {
                    if (viewHolder.itemId != 0.toLong()) {
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                val deleted = expenseViewModel.delete(
                                    (binding.valuesRecyclerView.adapter as ValuesAdapter).getValue(
                                        viewHolder.bindingAdapterPosition
                                    ) as Expense
                                )
                                withContext(Dispatchers.Main) {
                                    if (deleted > 0) {
                                        (binding.valuesRecyclerView.adapter as ValuesAdapter?)?.deleteItem(
                                            viewHolder.bindingAdapterPosition,
                                            true
                                        )
                                    } else {
                                        (binding.valuesRecyclerView.adapter as ValuesAdapter?)?.notifyItemChanged(
                                            viewHolder.bindingAdapterPosition,
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        (binding.valuesRecyclerView.adapter as ValuesAdapter).notifyItemChanged(
                            viewHolder.bindingAdapterPosition
                        )
                    }
                }
            }

        };

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(binding.valuesRecyclerView)

        binding.valuesRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )

        binding.valuesRecyclerView.adapter = ValuesAdapter(
            requireContext(),
            arrayListOf(),
            object : ValuesAdapter.OnItemClickListener {
                override fun onClick(item: ValueAndCategory) {
                    findNavController().navigate(
                        ExpensesFragmentDirections.actionExpensesFragmentToEditValueFragment(
                            item.value.id!!,
                            if (item.value is Income) 0 else 1
                        )
                    )
                }
            })
    }
}