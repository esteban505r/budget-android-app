package com.esteban.lopez.budget.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.esteban.lopez.budget.Application
import com.esteban.lopez.budget.R
import com.esteban.lopez.budget.databinding.FragmentMoneyBinding
import com.esteban.lopez.budget.view.Utils
import com.esteban.lopez.budget.viewmodel.ExpenseViewModel
import com.esteban.lopez.budget.viewmodel.ExpenseViewModelFactory
import com.esteban.lopez.budget.viewmodel.IncomeViewModel
import com.esteban.lopez.budget.viewmodel.IncomeViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MoneyFragment: Fragment() {

    lateinit var binding: FragmentMoneyBinding

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
    ): View {

        val view = FragmentMoneyBinding.inflate(layoutInflater)

        //Getting Incomes Reactively
        lifecycleScope.launch{
            incomeViewModel.getAll().collect {
                val iterator = it.listIterator()
                var result = 0.0
                while (iterator.hasNext()){
                    result += iterator.next().value.toDouble()
                }
                view.valueIncomes.text = "\$ ${Utils.formatNumberToString(result)}"

                val expenses = expenseViewModel.getAllAwaiting()
                val iteratorExpenses = expenses.listIterator()
                var resultExpenses = 0.0
                while (iteratorExpenses.hasNext()){
                    resultExpenses += iteratorExpenses.next().value.toDouble()
                }
                view.moneyGeneralValue.text = "\$ ${Utils.formatNumberToString(result - resultExpenses)}"
            }
        }

        lifecycleScope.launch {
            expenseViewModel.getAll().collect {
                val iterator = it.listIterator()
                var result = 0.0
                while (iterator.hasNext()){
                    result += iterator.next().value.toDouble()
                }
                view.valueExpenses.text = "\$ ${Utils.formatNumberToString(result)}"

                val incomes = incomeViewModel.getAllAwaiting()
                val iteratorIncomes = incomes.listIterator()
                var resultIncomes = 0.0
                while (iteratorIncomes.hasNext()){
                    resultIncomes += iteratorIncomes.next().value.toDouble()
                }
                view.moneyGeneralValue.text = "\$ ${Utils.formatNumberToString(resultIncomes - result)}"
            }
        }

        //Getting incomes
        lifecycleScope.launchWhenResumed {
            val incomes = incomeViewModel.getAllAwaiting()
            var result = 0.0
            view.incomesItemsTxt.text = "${incomes.size} Items"
            val iterator = incomes.listIterator()
            while (iterator.hasNext()){
                result += iterator.next().value.toDouble()
            }
            view.valueIncomes.text = "\$ ${Utils.formatNumberToString(result)}"

            val expenses = expenseViewModel.getAllAwaiting()
            var result2 = 0.0
            view.expensesItemsTxt.text = "${expenses.size} Items"
            val iterator2 = expenses.listIterator()
            while (iterator2.hasNext()){
                result2 += iterator2.next().value.toDouble()
            }
            view.valueExpenses.text = "\$ ${Utils.formatNumberToString(result2)}"

            view.moneyGeneralValue.text = "\$ ${Utils.formatNumberToString(result - result2)}"
        }

        view.expensesCardView.setOnClickListener{
            parentFragment?.parentFragment?.findNavController()?.navigate(R.id.action_homeFragment_to_expensesFragment)
        }

        view.incomesCardView.setOnClickListener{
            parentFragment?.parentFragment?.findNavController()?.navigate(R.id.action_homeFragment_to_incomesFragment)
        }


        return view.root
    }
}