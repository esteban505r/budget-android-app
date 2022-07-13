package com.esteban.lopez.budget.view.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esteban.lopez.budget.R
import com.esteban.lopez.budget.model.db.entities.Category
import com.esteban.lopez.budget.view.Utils
import com.esteban.lopez.budget.view.Utils.Companion.removeCommas

class BudgetEditAdapter(
    val context: Context,
    private val categories: ArrayList<Category>,
    private val onDeleteListener: OnDeleteListener,
    private val onEditCategory: OnEditCategory
) : RecyclerView.Adapter<BudgetEditAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameCategory = itemView.findViewById<TextView>(R.id.nameCategoryText);
        val budgetEditText = itemView.findViewById<EditText>(R.id.budgetTextView)
        val deleteImage = itemView.findViewById<ImageView>(R.id.img_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.budget_edit_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Listening value changes

        var current = ""
        holder.budgetEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(et: Editable?) {
                try {
                    if (current != et.toString()) {
                        holder.budgetEditText.removeTextChangedListener(this)
                        val formatedNumber = Utils.formatNumberToStringWithoutLetters(
                            if (et.toString().removeCommas().toDoubleOrNull() != null)
                                et.toString().removeCommas().toDouble()
                            else current.toDouble()
                        )

                        holder.budgetEditText.setText(formatedNumber)
                        holder.budgetEditText.setSelection(holder.budgetEditText.text.toString().lastIndex + 1)
                        current = et.toString()
                        holder.budgetEditText.addTextChangedListener(this)
                    }

                    if(holder.budgetEditText.hasFocus()){
                        if (et.toString().removeCommas().toDoubleOrNull() != null) {
                            onEditCategory.onEditCategory(
                                categories[position].copy(
                                    budget = et.toString().removeCommas().toDouble()
                                )
                            )
                        }
                    }

                } catch (e: Exception) {
                    holder.budgetEditText.removeTextChangedListener(this)
                    holder.budgetEditText.addTextChangedListener(this)
                    e.printStackTrace()
                }
            }

        })

        holder.nameCategory.text = categories[position].name
        holder.budgetEditText?.setText(categories[position].budget.toString())
        holder.deleteImage.setOnClickListener {
            onDeleteListener.onDelete(categories[position])
        }
    }

    fun changeData(list: List<Category>) {
        this.categories.clear()
        this.categories.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    interface OnDeleteListener {
        fun onDelete(category: Category)
    }

    interface OnEditCategory {
        fun onEditCategory(category: Category)
    }
}