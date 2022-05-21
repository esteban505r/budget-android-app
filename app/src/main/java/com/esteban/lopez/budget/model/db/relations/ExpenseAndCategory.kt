package com.esteban.lopez.budget.model.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.esteban.lopez.budget.model.db.entities.Category
import com.esteban.lopez.budget.model.db.entities.Expense
import com.esteban.lopez.budget.model.db.entities.Income
import com.esteban.lopez.budget.model.db.entities.ValueAndCategory

data class ExpenseAndCategory(
    @Embedded override val value: Expense,
    @Relation(
         parentColumn = "category",
         entityColumn = "id"
    )
    override val category:Category):ValueAndCategory