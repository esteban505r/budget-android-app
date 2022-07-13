package com.esteban.lopez.budget.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteban.lopez.budget.Application
import com.esteban.lopez.budget.R
import com.esteban.lopez.budget.databinding.FragmentMoneyBinding
import com.esteban.lopez.budget.view.Utils
import com.esteban.lopez.budget.view.adapters.BudgetAdapter
import com.esteban.lopez.budget.viewmodel.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MoneyFragment: Fragment() {

    lateinit var binding: FragmentMoneyBinding
    lateinit var adapter: BudgetAdapter

    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((activity?.application as Application).incomeRepository)
    }

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((activity?.application as Application).expenseRepository)
    }

    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory((activity?.application as Application).categoryRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMoneyBinding.inflate(layoutInflater)

       getAllData()

        binding.budgetRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        adapter = BudgetAdapter(requireContext(), arrayListOf())
        binding.budgetRecyclerView.adapter = adapter

        binding.expensesCardView.setOnClickListener{
            parentFragment?.parentFragment?.findNavController()?.navigate(R.id.action_homeFragment_to_expensesFragment)
        }

        binding.incomesCardView.setOnClickListener{
            parentFragment?.parentFragment?.findNavController()?.navigate(R.id.action_homeFragment_to_incomesFragment)
        }


        return binding.root
    }

    private fun getAllData() {
        //Getting Incomes Reactively
        lifecycleScope.launch{
            incomeViewModel.getAll().collect {
                val iterator = it.listIterator()
                var result = 0.0
                while (iterator.hasNext()){
                    result += iterator.next().value.toDouble()
                }
                binding.valueIncomes.text = "\$ ${Utils.formatNumberToString(result)}"

                val expenses = expenseViewModel.getAllAwaiting()
                val iteratorExpenses = expenses.listIterator()
                var resultExpenses = 0.0
                while (iteratorExpenses.hasNext()){
                    resultExpenses += iteratorExpenses.next().value.toDouble()
                }
                binding.moneyGeneralValue.text = "\$ ${Utils.formatNumberToString(result - resultExpenses)}"
            }
        }

        lifecycleScope.launch {
            expenseViewModel.getAll().collect {
                val iterator = it.listIterator()
                var result = 0.0
                while (iterator.hasNext()){
                    result += iterator.next().value.toDouble()
                }
                binding.valueExpenses.text = "\$ ${Utils.formatNumberToString(result)}"

                val incomes = incomeViewModel.getAllAwaiting()
                val iteratorIncomes = incomes.listIterator()
                var resultIncomes = 0.0
                while (iteratorIncomes.hasNext()){
                    resultIncomes += iteratorIncomes.next().value.toDouble()
                }
                binding.moneyGeneralValue.text = "\$ ${Utils.formatNumberToString(resultIncomes - result)}"
            }
        }

        //Getting incomes
        lifecycleScope.launchWhenResumed {
            val incomes = incomeViewModel.getAllAwaiting()
            var result = 0.0
            binding.incomesItemsTxt.text = "${incomes.size} Items"
            val iterator = incomes.listIterator()
            while (iterator.hasNext()){
                result += iterator.next().value.toDouble()
            }
            binding.valueIncomes.text = "\$ ${Utils.formatNumberToString(result)}"

            val expenses = expenseViewModel.getAllAwaiting()
            var result2 = 0.0
            binding.expensesItemsTxt.text = "${expenses.size} Items"
            val iterator2 = expenses.listIterator()
            while (iterator2.hasNext()){
                result2 += iterator2.next().value.toDouble()
            }
            binding.valueExpenses.text = "\$ ${Utils.formatNumberToString(result2)}"

            binding.moneyGeneralValue.text = "\$ ${Utils.formatNumberToString(result - result2)}"
        }


        lifecycleScope.launch{
            categoryViewModel.getAll().collect {
                adapter.changeData(it)
            }
        }
    }


}