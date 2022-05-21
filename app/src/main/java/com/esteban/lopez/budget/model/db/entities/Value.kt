package com.esteban.lopez.budget.model.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
interface Value{
    val id:Int?
    val value:Double
    val name:String
    val description:String?
    val category:Int?
    val date: Long
}