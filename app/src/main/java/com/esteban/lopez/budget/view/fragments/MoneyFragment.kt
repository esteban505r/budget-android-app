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

class MoneyFragment : Fragment() {

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


        return binding.root
    }



}