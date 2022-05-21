package com.esteban.lopez.budget.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esteban.lopez.budget.R

class BudgetAdapter(val context: Context, private val categories:Array<String>): RecyclerView.Adapter<BudgetAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameCategory = itemView.findViewById<TextView>(R.id.nameCategoryText);
        val budgetEditText = itemView.findViewById<EditText>(R.id.budgetEditText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.budget_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameCategory.text = categories[position]
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}