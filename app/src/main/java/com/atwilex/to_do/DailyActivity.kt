package com.atwilex.to_do

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atwilex.to_do.AppDependencies.appRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.zip.Inflater


@Suppress("DEPRECATION")
class DailyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily)


        //Initialization activity's elements
        val buttonComeBack : ImageButton = findViewById(R.id.comeBack)
        val buttonEdit : Button = findViewById(R.id.edit_button)
        val buttonAdd : Button = findViewById(R.id.add)
        val newTask : EditText = findViewById(R.id.NewTask)


        //Initialization List with adapter
        val taskList : RecyclerView = findViewById(R.id.ListDailyTasks)
        val list = mutableListOf<DailyDbEntity>()
        val complete : TextView = findViewById(R.id.complete)
        taskList.layoutManager = LinearLayoutManager(this)

        //Initialization var for modes
        var isEdit = false

        //Adapter initialization
        val adapter = DailyListAdapter(this, list, appRepository,
            //Callback 1, checking completed tasks
            {complete.text = isCompletedTasks(list)},
            //Callback 2, checking mode on click
            {dailyDbEntity, ->
                if (isEdit){
                    Toast.makeText(this, "Mode is Edit", Toast.LENGTH_SHORT).show()

                    val inflater = LayoutInflater.from(this)

                    val dialogView = inflater.inflate(R.layout.dialog_edit_task, null)

                    val editText = dialogView.findViewById<EditText>(R.id.renameTaskEditText)

                    editText.setText(dailyDbEntity.name)

                    MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.Dialog_title))
                        .setView(dialogView)
                        .setPositiveButton(getString(R.string.Rename_Edit_Button))
                        { dialog, _ ->
                            val newTaskName = editText.text.toString()
                            if(newTaskName.isNotEmpty()){
                                lifecycleScope.launch {
                                    appRepository.updateDailyData(dailyDbEntity.copy(name = newTaskName))
                                }
                                val index = list.indexOfFirst { it.id == dailyDbEntity.id }
                                if(index != -1){
                                    list[index].name = newTaskName
                                }
                                dialog.dismiss()
                            }
                        }
                        .setNegativeButton(getString(R.string.Rename_Cancel_Button)) { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            })

        taskList.adapter = adapter


        //Streak
        val streak : TextView = findViewById(R.id.streak)

        //Update list, streak with values from database
        lifecycleScope.launch {
            try {
                streak.text = appRepository.getStreak().streak.toString()
            } catch (e : NullPointerException){
                appRepository.insertStreak(AdditionalDbEntity(1L, 0))
                streak.text = appRepository.getStreak().streak.toString()
            }
            val items = appRepository.getDailyTab()
            list.clear()
            list.addAll(items)
            adapter.notifyItemRangeInserted(0, items.size)
            complete.text = isCompletedTasks(list)
        }


        //Swapping in list
        val itemTouchHelperCallback = object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = 0
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                (recyclerView.adapter as? DailyListAdapter)?.onItemMove(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

            //Animation
            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.7f
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1.0f
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(taskList)


        //Initialization edit's mode elements
        isEdit = false
        adapter.isEdit = false
        buttonAdd.visibility = View.GONE
        newTask.visibility = View.GONE


        //Return on the main activity
        buttonComeBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        //Create a new task
        buttonAdd.setOnClickListener {
            if (newTask.text.isNotBlank()){
                lifecycleScope.launch {
                    val newItem = DailyDbEntity(0, newTask.text.toString().trim(), 0L, list.size)
                    val id = appRepository.insertNewDailyData(newItem)
                    val newItemForList = newItem.copy(id = id)
                    list.add(newItemForList)
                    newTask.text.clear()
                    complete.text = isCompletedTasks(list)
                    adapter.notifyItemInserted(list.indexOf(newItemForList))
                }
                Toast.makeText(this, getString(R.string.Message_Created), Toast.LENGTH_SHORT).show()
            }
        }


        //Change daily mode
        buttonEdit.setOnClickListener {
            if(isEdit){ //Update to Use mode
                buttonAdd.visibility = View.GONE
                newTask.visibility = View.GONE
                isEdit = false
                adapter.isEdit = false
            } else { //Update to Edit mode
                buttonAdd.visibility = View.VISIBLE
                newTask.visibility = View.VISIBLE
                isEdit = true
                adapter.isEdit = true
            }
        }


        //I don't know what is it :)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //function for checking completed tasks
    fun isCompletedTasks(list : List<DailyDbEntity>): String{
        var isComplete = 0
        for (items in list){
            if(items.state == 1L) isComplete++
        }
        return "$isComplete/${list.size}"
    }
}