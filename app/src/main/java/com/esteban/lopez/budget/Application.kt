package com.esteban.lopez.budget

import android.app.Application
import com.esteban.lopez.budget.model.db.AppDatabase
import com.esteban.lopez.budget.model.repositories.CategoryRepository
import com.esteban.lopez.budget.model.repositories.ExpenseRepository
import com.esteban.lopez.budget.model.repositories.IncomeRepository

class Application : Application() {
    val database by lazy { AppDatabase.getDb(this) }
    val incomeRepository by lazy { IncomeRepository(database.incomeDAO()) }
    val expenseRepository by lazy { ExpenseRepository(database.expenseDAO()) }
    val categoryRepository by lazy { CategoryRepository(database.categoryDAO()) }
}