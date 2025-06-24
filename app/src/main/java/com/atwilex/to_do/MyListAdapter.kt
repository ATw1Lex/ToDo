package com.atwilex.to_do

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*

class MyListAdapter(context: Context,private val list: MutableList<DailyDbEntity>,private val tabRepository: DailyRepository, private val callback : () -> Unit) : ArrayAdapter<DailyDbEntity>(context,
    R.layout.list_black_text, R.id.list_content, list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertViewReturn : View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_black_text, parent, false)

        val textView: TextView = convertViewReturn.findViewById(R.id.list_content)
        val checkBox: CheckBox = convertViewReturn.findViewById(R.id.checkbox)
        val trashBin: ImageButton = convertViewReturn.findViewById(R.id.trash_bin)
        val item = list[position]

        textView.text = item.name
        checkBox.isChecked = item.state == 1L

        trashBin.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try { //if will exception here items will not delete from list
                    tabRepository.removeDataById(item.id)

                    list.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                    callback()
                } catch (e: Exception){
                    Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show()
                }
            }

        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.Main).launch {
                val updatedItem = item.copy(state = if (isChecked) 1L else 0L)
                tabRepository.updateData(updatedItem)
                list[position] = updatedItem
                callback()
            }
        }
        return convertViewReturn
    }
}