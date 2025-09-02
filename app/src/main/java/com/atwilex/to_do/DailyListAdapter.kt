package com.atwilex.to_do

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.util.Collections

class DailyListAdapter(private val context: Context, private val list: MutableList<DailyDbEntity>, private val tabRepository: AppRepository, private val callback : () -> Unit)
    : RecyclerView.Adapter<DailyListAdapter.TaskViewHolder>() {

    var isEdit = false
        set(value){
            field = value
            notifyDataSetChanged()
        }

    //Initialization
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.list_content)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        val trashBin: ImageButton = itemView.findViewById(R.id.trash_bin)
    }

    //Something...
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_black_text, parent, false)
        return TaskViewHolder(itemView)
    }

    //Set on click listeners
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var item = list[position]

        holder.textView.text = item.name
        holder.checkBox.isChecked = item.state != 0L

        if (isEdit){
            holder.trashBin.visibility = View.VISIBLE
        }
        else {
            holder.trashBin.visibility = View.GONE
        }

        //Deleting elements
        holder.trashBin.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                //Remove item from list, but with anti-bag defense
                try {
                    val actualPosition = holder.adapterPosition
                    item = list[actualPosition]
                    list.removeAt(actualPosition)
                    tabRepository.removeDailyDataById(item.id)
                    notifyItemRemoved(actualPosition)
                    //Update database
                    positionChecking()
                    Toast.makeText(context, context.getString(R.string.Message_Deleted), Toast.LENGTH_SHORT).show()
                    callback()
                }catch (e: Exception){
                    Toast.makeText(context, context.getString(R.string.Message_Slow_Down), Toast.LENGTH_SHORT).show()
                }
            }
        }

        //Checkbox updating
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.Main).launch {
                //Update checkbox, but with anti-bag defense
                try {
                    val actualPosition = holder.adapterPosition
                    item = list[actualPosition]
                    val updatedItem = item.copy(state = if (isChecked) 1L else 0L)
                    list[actualPosition] = updatedItem
                    tabRepository.updateDailyData(updatedItem)
                    notifyItemChanged(actualPosition)
                    callback()
                }catch (e: Exception){
                    Toast.makeText(context, context.getString(R.string.Message_Slow_Down), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Return list size
    override fun getItemCount() = list.size

    //Class for transfer the list's item between other items
    fun onItemMove(fromPosition : Int, toPosition : Int){

        //Swapping
        if(fromPosition < toPosition){
            Collections.swap(list, fromPosition, toPosition)
        }
        else {
            Collections.swap(list, toPosition, fromPosition)
        }
        notifyItemMoved(fromPosition, toPosition)

        //Update database
        positionChecking()
    }

    fun positionChecking(){
        for(item in list){
            if(item.position == list.indexOf(item)) continue
            val newItem = item.copy(position = list.indexOf(item))
            list[list.indexOf(item)] = newItem
            CoroutineScope(Dispatchers.Main).launch {
                tabRepository.updateDailyData(newItem)
            }
            notifyItemChanged(list.indexOf(item))
        }
    }
}
