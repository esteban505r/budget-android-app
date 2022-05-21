package com.esteban.lopez.budget.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.esteban.lopez.budget.model.db.dao.CategoryDAO
import com.esteban.lopez.budget.model.db.dao.ExpenseDAO
import com.esteban.lopez.budget.model.db.dao.IncomeDAO
import com.esteban.lopez.budget.model.db.entities.Category
import com.esteban.lopez.budget.model.db.entities.Expense
import com.esteban.lopez.budget.model.db.entities.Income

@Database(entities = [Income::class, Expense::class,Category::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun incomeDAO(): IncomeDAO
    abstract fun expenseDAO(): ExpenseDAO
    abstract fun categoryDAO(): CategoryDAO

    companion object{
        @Volatile
        var appDatabase:AppDatabase? = null
        fun getDb(context: Context):AppDatabase{
            if(appDatabase==null){
                appDatabase = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "budget"
                ).createFromAsset("database/budget.db").fallbackToDestructiveMigration().build()
            }
            return appDatabase!!;
        }
    }
}