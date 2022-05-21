package com.esteban.lopez.budget.model.repositories

import com.esteban.lopez.budget.model.db.dao.CategoryDAO
import com.esteban.lopez.budget.model.db.dao.ExpenseDAO
import com.esteban.lopez.budget.model.db.entities.Category
import com.esteban.lopez.budget.model.db.entities.Expense
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDAO: CategoryDAO){

    fun getAll(): Flow<List<Category>> {
        return categoryDAO.getAll();
    }

    suspend fun getAllAwaiting(): List<Category> {
        return categoryDAO.getAllAwaiting();
    }


}