package com.atwilex.to_do

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class DailyListAdapter(
    private val list: MutableList<DailyDbEntity>,
    private val callback1 : (DailyDbEntity) -> Unit,
    private val callback2 : (Int) -> Unit,
    private val callback3 : (Int) -> Unit) : RecyclerView.Adapter<DailyListAdapter.TaskViewHolder>() {

    var isEdit = false
        set(value){
            field = value
            notifyItemRangeChanged(0, list.size)
        }

    //Initialization
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.list_content)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        val trashBin: ImageButton = itemView.findViewById(R.id.trash_bin)
    }

    //Inflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_black_text, parent, false)
        return TaskViewHolder(itemView)
    }

    //Set on click listeners
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var item = list[position]
        holder.textView.text = item.name
        //holder.checkBox.isChecked = item.state != 0L

        if (isEdit){
            holder.trashBin.visibility = View.VISIBLE
        }
        else {
            holder.trashBin.visibility = View.GONE
        }

        //Edit textView's title
        holder.textView.setOnClickListener {
            callback1(list[holder.bindingAdapterPosition])
        }

        //Deleting elements
        holder.trashBin.setOnClickListener {
            callback2(holder.bindingAdapterPosition)
        }

        //Checkbox updating NEED UPDATE
        holder.checkBox.setOnClickListener {
            callback3(holder.bindingAdapterPosition)
        }
    }

    //Return list size
    override fun getItemCount() = list.size

    //Fun to transfer the list's item between other items
    fun onItemMove(fromPosition : Int, toPosition : Int){

        //Swapping
        if(fromPosition < toPosition){
            Collections.swap(list, fromPosition, toPosition)
        }
        else {
            Collections.swap(list, toPosition, fromPosition)
        }
        notifyItemMoved(fromPosition, toPosition)
    }
}
