package com.esteban.lopez.budget.view.fragments.dialogs

import android.R.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteban.lopez.budget.Application
import com.esteban.lopez.budget.R
import com.esteban.lopez.budget.databinding.BudgetDialogBinding
import com.esteban.lopez.budget.model.db.entities.Category
import com.esteban.lopez.budget.view.adapters.BudgetEditAdapter
import com.esteban.lopez.budget.viewmodel.CategoryViewModel
import com.esteban.lopez.budget.viewmodel.CategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class BudgetDialogFragment : DialogFragment() {

    private lateinit var binding: BudgetDialogBinding
    private lateinit var adapter: BudgetEditAdapter

    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory((activity?.application as Application).categoryRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(
            STYLE_NORMAL,
            style.Theme_Material_Light_NoActionBar
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BudgetDialogBinding.inflate(layoutInflater)

        binding.closeImgBtn.setOnClickListener {
            dismiss()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )

        adapter = BudgetEditAdapter(requireContext(),
            arrayListOf(), object : BudgetEditAdapter.OnDeleteListener {
                override fun onDelete(category: Category) {
                    lifecycleScope.launch {
                        val deleted = try {
                            categoryViewModel.delete(category)
                        } catch (e: Exception) {
                            -1
                        }

                        if (deleted <= 0) {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.error_deleting_category),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        else{
                            lifecycleScope.launch {
                                categoryViewModel.getAllAwaiting().let{
                                    adapter.changeData(it)
                                }
                            }
                        }
                    }
                }
            }, object : BudgetEditAdapter.OnEditCategory {
                override fun onEditCategory(category: Category) {
                    lifecycleScope.launch {
                        val updated = try {
                            categoryViewModel.update(category)
                        } catch (e: Exception) {
                            -1
                        }
                        if (updated <= 0) {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.error_updating_category),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            })

        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            categoryViewModel.getAllAwaiting().let{
                adapter.changeData(it)
            }
        }

        binding.addCategoryBtn.setOnClickListener {
            binding.cardView.visibility = View.VISIBLE
            binding.addCategoryBtn.visibility = View.GONE
        }

        binding.addCategoryOkBtn.setOnClickListener {
            if (binding.newCategoryNameEt.text.toString().isNotEmpty()) {
                lifecycleScope.launch {

                    val result: Long = try {
                        categoryViewModel.insert(
                            Category(
                                null,
                                name = binding.newCategoryNameEt.text.toString(),
                                budget = 0.0
                            )
                        )
                    } catch (e: Exception) {
                        -1
                    }

                    binding.cardView.visibility = View.GONE
                    binding.newCategoryNameEt.setText("")
                    binding.addCategoryBtn.visibility = View.VISIBLE

                    if (result <= 0) {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.error_inserting_category),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    else{
                        lifecycleScope.launch {
                            categoryViewModel.getAllAwaiting().let{
                                adapter.changeData(it)
                            }
                        }
                    }
                }
            }
        }

        binding.newCategoryCloseImgBtn.setOnClickListener {
            binding.cardView.visibility = View.GONE
            binding.newCategoryNameEt.setText("")
            binding.addCategoryBtn.visibility = View.VISIBLE
        }

        return binding.root
    }


    companion object {
        fun newInstance(): BudgetDialogFragment {
            val args = Bundle()

            val fragment = BudgetDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}