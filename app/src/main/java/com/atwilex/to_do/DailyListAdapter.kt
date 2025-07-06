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
        val item = list[position]

        holder.textView.text = item.name
        holder.checkBox.isChecked = item.state != 0L

        //Deleting elements
        holder.trashBin.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                //Remove item from list, but with anti-bag defense
                try {
                    list.removeAt(position)
                    tabRepository.removeDailyDataById(item.id)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                    callback()
                }catch (e: IndexOutOfBoundsException){
                    Toast.makeText(context, "Slow down please!", Toast.LENGTH_SHORT).show()
                    notifyItemRemoved(position)
                }
            }
        }

        //Checkbox updating
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.Main).launch {
                //Update checkbox, but with anti-bag defense
                try {
                    val updatedItem = item.copy(state = if (isChecked) 1L else 0L)
                    tabRepository.updateDailyData(updatedItem)
                    list[position] = updatedItem
                    callback()
                    notifyItemChanged(position)
                }catch (e: IndexOutOfBoundsException){
                    Toast.makeText(context, "Slow down please!", Toast.LENGTH_SHORT).show()
                    notifyItemRemoved(position)
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
            for(position in fromPosition until toPosition){
                Collections.swap(list, position, position+1)
            }
        }else {
            for(position in toPosition until fromPosition){
                Collections.swap(list, position, position-1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)

        //Update database
        CoroutineScope(Dispatchers.Main).launch {
            tabRepository.clearDailyTab()
            for (position in list.indices){
                tabRepository.insertNewDailyData(list[position])
            }
            notifyDataSetChanged()
        }
    }
}
