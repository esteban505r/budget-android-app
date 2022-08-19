package com.esteban.lopez.budget.model.db.dao

import androidx.room.*
import com.esteban.lopez.budget.model.db.entities.Expense
import com.esteban.lopez.budget.model.db.relations.ExpenseAndCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDAO {

    @Query("SELECT * FROM expense")
    fun getAll(): Flow<List<Expense>>

    @Query("SELECT * FROM expense")
    suspend fun getAllAwaiting(): List<Expense>

    @Insert
    suspend fun insertAll(vararg expense: Expense):List<Long>

    @Delete
    suspend fun delete(expense: Expense):Int

    @Query("SELECT * FROM expense WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Expense

    @Update
    suspend fun update(expense: Expense): Int

    @Transaction
    @Query("SELECT * FROM expense")
    fun getExpenseAndCategory(): Flow<List<ExpenseAndCategory>>

    @Transaction
    @Query("SELECT * FROM expense WHERE category = :categoryId")
    fun getExpenseAndCategoryByCategoryId(categoryId:Int): Flow<List<ExpenseAndCategory>>

    @Transaction
    @Query("SELECT * FROM expense WHERE category = :categoryId")
    suspend fun getExpenseAndCategoryByCategoryIdAwaiting(categoryId:Int):List<ExpenseAndCategory>

}