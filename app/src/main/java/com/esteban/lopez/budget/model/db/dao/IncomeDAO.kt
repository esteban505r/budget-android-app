package com.esteban.lopez.budget.model.db.dao

import androidx.room.*
import com.esteban.lopez.budget.model.db.entities.Income
import com.esteban.lopez.budget.model.db.relations.IncomeAndCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDAO {

    @Query("SELECT * FROM income")
    fun getAll(): Flow<List<Income>>

    @Query("SELECT * FROM income")
    suspend fun getAllAwaiting(): List<Income>

    @Insert
    suspend fun insertAll(vararg income: Income):List<Long>

    @Delete()
    suspend fun delete(income: Income):Int

    @Query("SELECT * FROM income WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Income

    @Update
    suspend fun update(income: Income): Int

    @Transaction
    @Query("SELECT * FROM income")
    fun getIncomesAndCategory(): Flow<List<IncomeAndCategory>>




}