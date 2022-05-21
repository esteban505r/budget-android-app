package com.esteban.lopez.budget.model.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true) override val id:Int? = null,
    @ColumnInfo override val value:Double,
    @ColumnInfo override val name:String,
    @ColumnInfo override val description:String? = null,
    @ColumnInfo override val category:Int? = null,
    override val date: Long = Date().time
):Value