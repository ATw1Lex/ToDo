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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DisposableListAdapter(context: Context, private val list: MutableList<DisposableDbEntity>, private val tabRepository: AppRepository) : ArrayAdapter<DisposableDbEntity>(context,
    R.layout.disposable_list_item, R.id.list_content, list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertViewReturn : View = convertView ?: LayoutInflater.from(context).inflate(R.layout.disposable_list_item, parent, false)

        //Initialization
        val textView: TextView = convertViewReturn.findViewById(R.id.list_content)
        val checkBox: CheckBox = convertViewReturn.findViewById(R.id.checkbox)
        val trashBin: ImageButton = convertViewReturn.findViewById(R.id.trash_bin)
        val item = list[position]
        textView.text = item.name


        //Remove item from list
        trashBin.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                    tabRepository.removeDisposableDataById(item.id)

                    list.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }

        }

        //Checkbox updating
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.Main).launch {
                tabRepository.removeDisposableDataById(item.id)
                list.removeAt(position)
                notifyDataSetChanged()
                Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show()
            }
        }

        return convertViewReturn
    }
}