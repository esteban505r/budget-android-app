package com.esteban.lopez.budget.view.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.esteban.lopez.budget.Application
import com.esteban.lopez.budget.R
import com.esteban.lopez.budget.databinding.FragmentHomeBinding
import com.esteban.lopez.budget.databinding.NewValueDialogBinding
import com.esteban.lopez.budget.model.db.entities.Category
import com.esteban.lopez.budget.model.db.entities.Expense
import com.esteban.lopez.budget.model.db.entities.Income
import com.esteban.lopez.budget.view.Utils
import com.esteban.lopez.budget.view.Utils.Companion.removeCommas
import com.esteban.lopez.budget.viewmodel.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

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
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root;

        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false

        binding.bottomNavigationView.setupWithNavController(navController)

        setupFab(binding)

        return view
    }

    private fun setupFab(binding: FragmentHomeBinding) {
        binding.fab.setOnClickListener {
            val binding = NewValueDialogBinding.inflate(layoutInflater,null,false);

           createDialog(binding)

            //Creating type value spinner adapter
//            ArrayAdapter.createFromResource(
//                requireContext(),
//                R.array.value_type,
//                android.R.layout.simple_spinner_item
//            ).also { adapter ->
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                binding.spinner.adapter = adapter
//            }

            binding.imageIncomeToggle.isActivated = true

            binding.imageExpensiveToggle.setOnClickListener{
                it.isActivated = true
                binding.imageIncomeToggle.isActivated = false
                binding.textView7.setText(R.string.expense)
            }

            binding.imageIncomeToggle.setOnClickListener{
                it.isActivated = !it.isActivated
                binding.imageExpensiveToggle.isActivated = false
                binding.textView7.setText(R.string.income)
            }

        }
    }

    private fun createDialog(binding: NewValueDialogBinding) {
        val valueEt = binding.valueTextInputLayout.editText

        var current = ""

        lifecycleScope.launch {
            val list = categoryViewModel.getAllAwaiting();
            binding.spinner.adapter = ArrayAdapter<Category>(requireContext(),android.R.layout.simple_spinner_dropdown_item, list)
        }
        //Listening value changes
        valueEt?.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(et: Editable?) {
                try{
                    if(current!=et.toString()){
                        valueEt.removeTextChangedListener(this)
                        val formatedNumber = Utils.formatNumberToStringWithoutLetters(
                            if (et.toString().removeCommas().toDoubleOrNull()!=null)
                                et.toString().removeCommas().toDouble()
                            else current.toDouble());

                        valueEt.setText(formatedNumber)
                        valueEt.setSelection(valueEt.text.toString().lastIndex+1)
                        current = et.toString()
                        valueEt.addTextChangedListener(this)
                    }

                }
                catch(e:Exception){
                    valueEt.removeTextChangedListener(this)
                    valueEt.addTextChangedListener(this)
                    e.printStackTrace()
                }
            }

        })

        val dialog = AlertDialog.Builder(requireContext()).setView(binding.root).setPositiveButton(android.R.string.ok
        ) { p0, p1 ->
            if (binding.nameTextInputLayout.editText?.text?.isNotEmpty() == true &&
                binding.descriptionTextField.editText?.text?.isNotEmpty() == true &&
                binding.valueTextInputLayout.editText?.text?.isNotEmpty() == true &&
                binding.valueTextInputLayout.editText?.text?.toString()?.removeCommas()?.isDigitsOnly() == true
            ) {
                if (binding.imageIncomeToggle.isActivated) {
                    //Income
                    lifecycleScope.launch {
                        val income = Income(
                            name = binding.nameTextInputLayout.editText!!.text!!.toString(),
                            description = binding.descriptionTextField.editText!!.text!!.toString(),
                            value = binding.valueTextInputLayout.editText!!.text!!.toString().removeCommas()
                                .toDouble(),
                            category = (binding.spinner.selectedItem as Category?)?.id?:0
                        );

                        val fieldsInserted = incomeViewModel.insert(income)
                        print("Se han insertado $fieldsInserted campos")
                    }
                }
                else{
                    //Expense
                    lifecycleScope.launch {
                        val expense = Expense(
                            name = binding.nameTextInputLayout.editText!!.text!!.toString(),
                            description = binding.descriptionTextField.editText!!.text!!.toString(),
                            value = binding.valueTextInputLayout.editText!!.text!!.toString().removeCommas()
                                .toDouble(),
                            category = (binding.spinner.selectedItem as Category?)?.id?:0
                        );

                        val fieldsInserted = expenseViewModel.insert(expense)
                        print("Se han insertado $fieldsInserted campos")
                    }
                }
            }
            else{
                if(binding.valueTextInputLayout.editText?.text?.toString()?.removeCommas()?.isDigitsOnly() == false){
                    Snackbar.make(this.binding.root,getString(R.string.error_digits_only),Snackbar.LENGTH_SHORT).show()
                }
                else{
                    Snackbar.make(this.binding.root,getString(R.string.error_empty_fields),Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }

}