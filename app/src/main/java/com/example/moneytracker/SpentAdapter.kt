package com.example.moneytracker

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class SpentAdapter(private var spent: List<Spent>):
    RecyclerView.Adapter<SpentAdapter.SpentHolder>() {
    class SpentHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val label : TextView = view.findViewById(R.id.label)
        val amount : TextView = view.findViewById(R.id.amount)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpentHolder {
        //create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spent_layout, parent, false)
        return SpentHolder(view)
    }
    override fun onBindViewHolder(holder: SpentHolder, position: Int) {
        val spent = spent[position]
        val context = holder.amount.context

        // for + -
        if (spent.amount >= 0) {
            holder.amount.text = "+ RM%.2f".format(spent.amount)
            holder.amount.setTextColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.holo_green_light
                )
            )
        } else {
            holder.amount.text = "- RM%.2f".format(abs(spent.amount))
            holder.amount.setTextColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.holo_red_dark
                )
            )
        }
        // for reading the data
        holder.label.text = spent.label
        holder.amount.text = spent.amount.toString()


        holder.itemView.setOnClickListener {
            val intent = Intent(context, ShowActivity::class.java)
            intent.putExtra("spents",spent)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return spent.size
    }

    // to update the adapter dataset
    fun setAdapterData(spent: List<Spent>){
        this.spent = spent
        notifyDataSetChanged()
    }

}