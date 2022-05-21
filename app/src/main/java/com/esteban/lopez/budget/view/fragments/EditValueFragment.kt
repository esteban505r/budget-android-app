package com.esteban.lopez.budget.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.esteban.lopez.budget.Application
import com.esteban.lopez.budget.R
import com.esteban.lopez.budget.databinding.EditValueFragmentBinding
import com.esteban.lopez.budget.model.db.entities.Expense
import com.esteban.lopez.budget.model.db.entities.Income
import com.esteban.lopez.budget.model.db.entities.Value
import com.esteban.lopez.budget.view.Utils.Companion.isADouble
import com.esteban.lopez.budget.view.Utils.Companion.removeCommas
import com.esteban.lopez.budget.viewmodel.ExpenseViewModel
import com.esteban.lopez.budget.viewmodel.ExpenseViewModelFactory
import com.esteban.lopez.budget.viewmodel.IncomeViewModel
import com.esteban.lopez.budget.viewmodel.IncomeViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class EditValueFragment : Fragment() {

    val args: EditValueFragmentArgs by navArgs()

    lateinit var binding: EditValueFragmentBinding

    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((activity?.application as Application).incomeRepository)
    }

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((activity?.application as Application).expenseRepository)
    }

    lateinit var value: Value

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditValueFragmentBinding.inflate(layoutInflater)

        val view = binding.root

        lifecycleScope.launch {
            value = if (args.type == 0) {
                incomeViewModel.getIncomeById(args.id)
            } else {
                expenseViewModel.getExpenseById(args.id)
            }

            binding.nameTextInputLayout.editText?.setText(value.name)
            binding.valueTextInputLayout.editText?.setText(value.value.toString())
            binding.descriptionTextField.editText?.setText(value.description)

            binding.button.setOnClickListener {
                lifecycleScope.launch {
                    val name = binding.nameTextInputLayout.editText?.text;
                    val description = binding.descriptionTextField.editText?.text
                    val valueT = binding.valueTextInputLayout.editText?.text
                    if (name?.isNotEmpty() == true &&
                        description?.isNotEmpty() == true &&
                        valueT?.isNotEmpty() == true && valueT.toString().removeCommas().isADouble()
                    ) {
                        if (args.type == 0) {
                            val income = (value as Income).copy(
                                name = name.toString(),
                                description = description.toString(),
                                value = valueT.toString().removeCommas().toDouble()
                            )
                            val result = incomeViewModel.update(income)
                            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                "success",
                                result > 0
                            )
                            findNavController().navigateUp()
                        } else {
                            val expense = (value as Expense).copy(
                                name = name.toString(),
                                description = description.toString(),
                                value = valueT.toString().removeCommas().toDouble()
                            )
                            val result = expenseViewModel.update(expense)
                            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                "success",
                                result > 0
                            )
                            findNavController().navigateUp()
                        }
                    } else {
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            "success",
                            false
                        )
                        Snackbar.make(
                            binding.root,
                            R.string.error_empty_fields,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        if (args.type == 0) {
            binding.imageIncomeToggle.isActivated = true
            binding.imageExpensiveToggle.isActivated = false
        } else {
            binding.imageExpensiveToggle.isActivated = true
            binding.imageIncomeToggle.isActivated = false
        }

        return view
    }

}