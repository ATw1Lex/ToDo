package com.atwilex.to_do

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StreakListAdapter(private val list: MutableList<DailyDbEntity>, ) : RecyclerView.Adapter<StreakListAdapter.TaskViewHolder>() {

    //Initialization
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.list_content)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    //Inflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.streak_list_item, parent, false)
        return TaskViewHolder(itemView)
    }

    //Set on click listeners
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var item = list[position]
        holder.textView.text = item.name
        holder.checkBox.isChecked = item.state != 0L
    }

    //Return list size
    override fun getItemCount() = list.size
}