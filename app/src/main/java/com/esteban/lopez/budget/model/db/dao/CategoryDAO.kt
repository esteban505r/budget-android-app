package com.esteban.lopez.budget.model.db.dao

import androidx.room.*
import com.esteban.lopez.budget.model.db.entities.Category
import com.esteban.lopez.budget.model.db.entities.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {

    @Query("SELECT * FROM category")
    fun getAll(): Flow<List<Category>>

    @Query("SELECT * FROM category")
    suspend fun getAllAwaiting(): List<Category>
}