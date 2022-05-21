package com.esteban.lopez.budget.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.esteban.lopez.budget.model.db.entities.Expense
import com.esteban.lopez.budget.model.db.relations.ExpenseAndCategory
import com.esteban.lopez.budget.model.db.relations.IncomeAndCategory
import com.esteban.lopez.budget.model.repositories.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class ExpenseViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    suspend fun getAllAwaiting(): List<Expense> {
        return expenseRepository.getAllAwaiting()
    }

    fun getAll(): Flow<List<Expense>> {
        return expenseRepository.getAll()
    }

    fun getExpensesAndCategory(): Flow<List<ExpenseAndCategory>> {
        return expenseRepository.getExpenseAndCategory()
    }

    suspend fun insert(expense: Expense): List<Long>{
        return expenseRepository.insert(expense)
    }

    suspend fun delete(expense: Expense):Int{
        return expenseRepository.delete(expense);
    }

    suspend fun getExpenseById(id: Int): Expense {
        return expenseRepository.getExpenseById(id)
    }

    suspend fun update(expense: Expense): Int {
        return expenseRepository.update(expense)
    }
}

class ExpenseViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}