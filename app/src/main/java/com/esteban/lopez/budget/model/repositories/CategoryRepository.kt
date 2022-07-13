package com.esteban.lopez.budget.model.repositories

import com.esteban.lopez.budget.model.db.dao.CategoryDAO
import com.esteban.lopez.budget.model.db.entities.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDAO: CategoryDAO){

    fun getAll(): Flow<List<Category>> {
        return categoryDAO.getAll();
    }

    suspend fun getAllAwaiting(): List<Category> {
        return categoryDAO.getAllAwaiting();
    }

    suspend fun insert(category: Category): Long {
        return categoryDAO.insert(category)
    }

    suspend fun delete(category: Category):Int {
        return categoryDAO.delete(category)
    }

    suspend fun update(category: Category):Int {
        return categoryDAO.update(category)
    }


}