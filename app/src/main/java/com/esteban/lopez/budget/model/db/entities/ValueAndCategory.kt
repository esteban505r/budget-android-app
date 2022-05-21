package com.esteban.lopez.budget.model.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
interface ValueAndCategory{
    val value:Value
    val category:Category
}