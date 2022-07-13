package com.esteban.lopez.budget.view.fragments.dialogs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.esteban.lopez.budget.Application
import com.esteban.lopez.budget.R
import com.esteban.lopez.budget.databinding.NewValueDialogBinding
import com.esteban.lopez.budget.model.db.entities.Category
import com.esteban.lopez.budget.model.db.entities.Expense
import com.esteban.lopez.budget.model.db.entities.Income
import com.esteban.lopez.budget.view.Utils
import com.esteban.lopez.budget.view.Utils.Companion.removeCommas
import com.esteban.lopez.budget.viewmodel.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class NewValueDialogFragment : DialogFragment() {

    lateinit var binding: NewValueDialogBinding

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
    ): View? {
        binding = NewValueDialogBinding.inflate(layoutInflater)
        val valueEt = binding.valueTextInputLayout.editText

        binding.imageIncomeToggle.isActivated = true

        binding.imageExpensiveToggle.setOnClickListener {
            it.isActivated = true
            binding.imageIncomeToggle.isActivated = false
            binding.textView7.setText(com.esteban.lopez.budget.R.string.expense)
        }

        binding.imageIncomeToggle.setOnClickListener {
            it.isActivated = !it.isActivated
            binding.imageExpensiveToggle.isActivated = false
            binding.textView7.setText(com.esteban.lopez.budget.R.string.income)
        }




        lifecycleScope.launch {
            val list = categoryViewModel.getAllAwaiting();
            binding.spinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item, list
            )
        }


        //Listening value changes
        valueEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(et: Editable?) {
                try{
                    valueEt.removeTextChangedListener(this)
                    val formatedNumber = Utils.formatNumberToStringWithoutLetters(
                        et.toString().removeCommas().toDoubleOrNull()
                    )
                    valueEt.setText(formatedNumber)
                    valueEt.setSelection(valueEt.text.toString().lastIndex + 1)
                    valueEt.addTextChangedListener(this)
                }
                catch(e:Exception){
                    e.printStackTrace()
                }
            }

        })


        binding.button3.setOnClickListener {

            if (binding.nameTextInputLayout.editText?.text?.isNotEmpty() == true &&
                binding.descriptionTextField.editText?.text?.isNotEmpty() == true &&
                binding.valueTextInputLayout.editText?.text?.isNotEmpty() == true &&
                binding.valueTextInputLayout.editText?.text?.toString()?.removeCommas()
                    ?.isDigitsOnly() == true
            ) {
                if (binding.imageIncomeToggle.isActivated) {
                    //Income
                    lifecycleScope.launch {
                        val income = Income(
                            name = binding.nameTextInputLayout.editText?.text?.toString() ?: "",
                            description = binding.descriptionTextField.editText?.text?.toString(),
                            value = binding.valueTextInputLayout.editText?.text?.toString()
                                ?.removeCommas()
                                ?.toDouble() ?: 0.0,
                            category = (binding.spinner.selectedItem as Category?)?.id ?: 0
                        );

                        val fieldsInserted = incomeViewModel.insert(income)
                        print("Se han insertado $fieldsInserted campos")
                    }
                } else {
                    //Expense
                    lifecycleScope.launch {
                        val expense = Expense(
                            name = binding.nameTextInputLayout.editText?.text?.toString() ?: "",
                            description = binding.descriptionTextField.editText?.text?.toString(),
                            value = binding.valueTextInputLayout.editText?.text?.toString()
                                ?.removeCommas()
                                ?.toDouble() ?: 0.0,
                            category = (binding.spinner.selectedItem as Category?)?.id ?: 0
                        );

                        val fieldsInserted = expenseViewModel.insert(expense)
                        print("Se han insertado $fieldsInserted campos")
                    }
                }
                dismiss()
            } else {
                if (binding.valueTextInputLayout.editText?.text?.toString()?.removeCommas()
                        ?.isDigitsOnly() == false
                ) {
                    Snackbar.make(
                        this.binding.root,
                        getString(com.esteban.lopez.budget.R.string.error_digits_only),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        this.binding.root,
                        getString(com.esteban.lopez.budget.R.string.error_empty_fields),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }


        return binding.root
    }

    companion object {
        fun newInstance(): NewValueDialogFragment {
            val args = Bundle()
            val fragment = NewValueDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}