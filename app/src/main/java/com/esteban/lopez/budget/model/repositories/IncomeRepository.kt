package com.esteban.lopez.budget.model.repositories

import com.esteban.lopez.budget.model.db.dao.IncomeDAO
import com.esteban.lopez.budget.model.db.entities.Income
import com.esteban.lopez.budget.model.db.relations.IncomeAndCategory
import kotlinx.coroutines.flow.Flow

class IncomeRepository(private val incomeDAO: IncomeDAO){

    fun getAll(): Flow<List<Income>> {
        return incomeDAO.getAll();
    }

    suspend fun getAllAwaiting(): List<Income> {
        return incomeDAO.getAllAwaiting();
    }

    suspend fun insert(income: Income): List<Long> {
        return incomeDAO.insertAll(income);
    }

    suspend fun delete(income: Income): Int {
        return incomeDAO.delete(income)
    }

    suspend fun getIncomeById(id: Int): Income {
        return incomeDAO.getById(id)
    }

    suspend fun update(income: Income): Int {
        return  incomeDAO.update(income)
    }

    fun getIncomeAndCategory():Flow<List<IncomeAndCategory>>{
        return incomeDAO.getIncomesAndCategory();
    }
}