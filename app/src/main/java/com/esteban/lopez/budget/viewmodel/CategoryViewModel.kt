package com.esteban.lopez.budget.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.esteban.lopez.budget.model.db.entities.Category
import com.esteban.lopez.budget.model.db.entities.Expense
import com.esteban.lopez.budget.model.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow

class CategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {

    suspend fun getAllAwaiting(): List<Category> {
        return categoryRepository.getAllAwaiting()
    }

    fun getAll(): Flow<List<Category>> {
        return categoryRepository.getAll()
    }

    suspend fun insert(category: Category):Long{
        return categoryRepository.insert(category)
    }

    suspend fun delete(category: Category):Int {
        return categoryRepository.delete(category)
    }

    suspend fun update(category: Category):Int {
        return categoryRepository.update(category)
    }

}

class CategoryViewModelFactory(private val repository: CategoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}