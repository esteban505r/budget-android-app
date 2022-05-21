package com.esteban.lopez.budget.model.repositories

import com.esteban.lopez.budget.model.db.dao.ExpenseDAO
import com.esteban.lopez.budget.model.db.entities.Expense
import com.esteban.lopez.budget.model.db.relations.ExpenseAndCategory
import com.esteban.lopez.budget.model.db.relations.IncomeAndCategory
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDAO: ExpenseDAO){

    fun getAll(): Flow<List<Expense>> {
        return expenseDAO.getAll();
    }

    suspend fun getAllAwaiting(): List<Expense> {
        return expenseDAO.getAllAwaiting();
    }

    suspend fun insert(expense: Expense): List<Long> {
        return expenseDAO.insertAll(expense);
    }

    suspend fun delete(expense: Expense): Int {
        return expenseDAO.delete(expense)
    }

    suspend fun getExpenseById(id: Int): Expense {
        return expenseDAO.getById(id)
    }

    suspend fun update(expense: Expense): Int {
        return expenseDAO.update(expense)
    }

    fun getExpenseAndCategory():Flow<List<ExpenseAndCategory>>{
        return expenseDAO.getExpenseAndCategory();
    }
}