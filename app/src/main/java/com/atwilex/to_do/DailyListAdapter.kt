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

class DailyListAdapter(private val context: Context, private val list: MutableList<DailyDbEntity>, private val tabRepository: AppRepository, private val callback : () -> Unit)
    : RecyclerView.Adapter<DailyListAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Initialization
        val textView: TextView = itemView.findViewById(R.id.list_content)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        val trashBin: ImageButton = itemView.findViewById(R.id.trash_bin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_black_text, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = list[position]

        holder.textView.text = item.name
        holder.checkBox.isChecked = item.state != 0L

        //Deleting elements
        holder.trashBin.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                //Remove item from list
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
                val updatedItem = item.copy(state = if (isChecked) 1L else 0L)
                tabRepository.updateDailyData(updatedItem)
                list[position] = updatedItem
                callback()
                notifyItemChanged(position)
            }
        }
    }


    override fun getItemCount() = list.size
}
