package com.esteban.lopez.budget.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.esteban.lopez.budget.model.db.entities.Income
import com.esteban.lopez.budget.model.db.relations.IncomeAndCategory
import com.esteban.lopez.budget.model.repositories.IncomeRepository
import kotlinx.coroutines.flow.Flow

class IncomeViewModel(private val incomeRepository: IncomeRepository) : ViewModel() {

   suspend fun update(income: Income):Int{
        return incomeRepository.update(income)
    }

    fun getAll(): Flow<List<Income>> {
        return incomeRepository.getAll()
    }

    fun getIncomesAndCategory(): Flow<List<IncomeAndCategory>> {
        return incomeRepository.getIncomeAndCategory()
    }

    suspend fun getAllAwaiting(): List<Income> {
        return incomeRepository.getAllAwaiting()
    }

    suspend fun getIncomeById(id:Int):Income{
        return incomeRepository.getIncomeById(id)
    }

    suspend fun insert(income: Income): List<Long>{
        return incomeRepository.insert(income)
    }

    suspend fun delete(income:Income):Int{
        return incomeRepository.delete(income);
    }
}

class IncomeViewModelFactory(private val repository: IncomeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}