package com.esteban.lopez.budget.model.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id:Int? = null,
    @ColumnInfo val name:String,
    @ColumnInfo val budget:Double,
){
    override fun toString(): String {
        return name;
    }
}