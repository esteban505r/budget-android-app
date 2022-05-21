package com.esteban.lopez.budget.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.esteban.lopez.budget.R
import com.esteban.lopez.budget.model.db.entities.Income
import com.esteban.lopez.budget.model.db.entities.Value
import java.text.SimpleDateFormat
import java.util.*

class CategoriesSpinnerAdapter(private val context: Context, private var values:MutableList<Value>): RecyclerView.Adapter<CategoriesSpinnerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.nameTextView)
        val value = itemView.findViewById<TextView>(R.id.valueTextView)
        val date = itemView.findViewById<TextView>(R.id.dateTextView)
        var type = 0 // 0 - Income -- 1 - Expense
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_value,parent,false)
        return ViewHolder(view)
    }

    fun changeData(values:List<Value>,updating:Boolean = false){
        this.values.clear()
        this.values.addAll(values)
        if(updating) notifyDataSetChanged()
    }

    fun changeItem(value:Value,position: Int,updating:Boolean = false){
        this.values[position] = value
        if(updating) notifyItemChanged(position)
    }

    fun getValue(position: Int):Value{
        return values[position]
    }

    fun deleteItem(position: Int,updating:Boolean = false){
        this.values.removeAt(position)
        if(updating)  notifyItemRemoved(position)
    }

    override fun getItemId(position: Int): Long {
        return values[position].id?.toLong()?:0
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = values[position].name

        holder.value.text = "${if(values[position] is Income) '+' else '-' } ${values[position].value.toString()}"

        holder.value.setTextColor(
            if(values[position] is Income)
            ContextCompat.getColor(context,android.R.color.holo_green_dark)
            else
            ContextCompat.getColor(context,android.R.color.holo_red_light)
        )

        val date = Date(values[position].date)
        val formatedDate: String = SimpleDateFormat("dd-MM-yyyy HH:mm",Locale.ENGLISH).format(Date())
        holder.date.text = formatedDate
        holder.type = if(values[position] is Income) 0 else 1
    }

    override fun getItemCount(): Int {
        return values.size
    }

}
