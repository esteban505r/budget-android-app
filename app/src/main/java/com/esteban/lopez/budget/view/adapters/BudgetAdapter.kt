package com.esteban.lopez.budget.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esteban.lopez.budget.R
import com.esteban.lopez.budget.model.db.entities.Category
import com.esteban.lopez.budget.view.Utils
import com.esteban.lopez.budget.view.Utils.Companion.removeCommas

class BudgetAdapter(
    val context: Context,
    private val categories: ArrayList<Category>
) : RecyclerView.Adapter<BudgetAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameCategory = itemView.findViewById<TextView>(R.id.nameCategoryText);
        val budgetTextView = itemView.findViewById<TextView>(R.id.budgetTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.budget_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Listening value changes
        holder.nameCategory.text = categories[position].name
        holder.budgetTextView?.text = categories[position].budget.toString()

        val formatedNumber = Utils.formatNumberToStringWithoutLetters(
            if (holder.budgetTextView?.text?.toString()?.removeCommas()?.toDoubleOrNull() != null)
                holder.budgetTextView.text?.toString()?.removeCommas()?.toDouble()
            else 0.0
        )

        holder.budgetTextView?.text = formatedNumber
    }

    fun changeData(list: List<Category>) {
        this.categories.clear()
        this.categories.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return categories.size
    }

}