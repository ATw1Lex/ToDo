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
import kotlinx.coroutines.launch
import java.time.LocalDate


@Suppress("DEPRECATION")
class DailyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily)

        //Initialization activity's elements
        val buttonComeBack : ImageButton = findViewById(R.id.comeBack)
        val buttonEdit : ImageButton = findViewById(R.id.edit_button)
        val buttonAdd : Button = findViewById(R.id.add)
        val newTask : EditText = findViewById(R.id.NewTask)

        //Initialization List with adapter
        val listView : RecyclerView = findViewById(R.id.ListDailyTasks)
        val taskList = mutableListOf<DailyDbEntity>()
        val complete: TextView = findViewById(R.id.complete)
        listView.layoutManager = LinearLayoutManager(this)

        //Initialization isEdit for modes
        var isEdit = false

        //Late initialization to use update adapter in adapter constructor
        lateinit var adapter : DailyListAdapter

        //Lambda for update completed tasks
        val isCompleted = {complete.text = isCompletedTasks(taskList)}
        //Lambda for update positions in list
        val positionsUpdate = {
            for(item in taskList){
                if(item.position == taskList.indexOf(item)) continue
                val newItem = item.copy(position = taskList.indexOf(item))
                taskList[taskList.indexOf(item)] = newItem
                lifecycleScope.launch { appRepository.updateDailyData(newItem) }
                adapter.notifyItemChanged(taskList.indexOf(item))
            }}

        //Adapter initialization
        adapter = DailyListAdapter(taskList,
            //Callback 1 renaming elements
            {dailyDbEntity ->
                if (isEdit){
                    try {
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
                                        val items = appRepository.getToday(LocalDate.now().toString())
                                        taskList.clear()
                                        taskList.addAll(items)
                                        //Adapter notify
                                        adapter.notifyItemRangeRemoved(0, items.size)
                                        adapter.notifyItemRangeInserted(0, items.size)
                                    }
                                    dialog.dismiss()
                                }

                            }
                            .setNegativeButton(getString(R.string.Rename_Cancel_Button)) { dialog, _ -> dialog.dismiss() }
                            .show()
                    } catch (e : Exception){
                        e.printStackTrace()
                    }
                }
            },
            //Callback 2 deleting elements
            {actualPosition ->
                    try {
                        lifecycleScope.launch {
                            val item = taskList[actualPosition]
                            taskList.removeAt(actualPosition)
                            appRepository.removeDailyDataById(item.id)
                            adapter.notifyItemRemoved(actualPosition)
                        }
                        //Update database
                        positionsUpdate()
                        Toast.makeText(this, getString(R.string.Message_Deleted), Toast.LENGTH_SHORT).show()
                        isCompleted()
                    }catch (e: Exception){
                        e.printStackTrace()
                        Toast.makeText(this, getString(R.string.Message_Slow_Down), Toast.LENGTH_SHORT).show()
                    }
            },
            //Callback 3 updating checkbox
            {actualPosition, isChecked ->
                try {
                    if (isChecked){
                        taskList[actualPosition].state = 1L
                        lifecycleScope.launch { appRepository.updateDailyData(taskList[actualPosition]) }
                        isCompleted()
                    } else {
                        taskList[actualPosition].state = 0L
                        lifecycleScope.launch { appRepository.updateDailyData(taskList[actualPosition]) }
                        isCompleted()
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
        )

        //Set up adapter
        listView.adapter = adapter

        //Streak
        val streak : TextView = findViewById(R.id.streak)
        val streakButton : ImageButton = findViewById(R.id.streakButton)

        //Update list, streak with values from database
        lifecycleScope.launch {
            //If streak is existing
            try {
                streak.text = "  " + appRepository.getStreak().streak.toString()
            //If streak isn't existing
            } catch (e : NullPointerException){
                e.printStackTrace()
                appRepository.insertStreak(AdditionalDbEntity(1L, 0))
                streak.text = appRepository.getStreak().streak.toString()
            }
            //Get Items
            val items = appRepository.getToday(LocalDate.now().toString())
            //Clear + Add new items
            taskList.clear()
            taskList.addAll(items)
            //Adapter update
            adapter.notifyItemRangeInserted(0, items.size)
            //Completed tasks update
            isCompleted()
        }


        //Swapping logic in list
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
                positionsUpdate()
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
        //Swapping logic set up
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(listView)


        //Initialization edit's mode elements
        isEdit = false
        adapter.isEdit = false
        buttonAdd.visibility = View.GONE
        newTask.visibility = View.GONE

        //Go to streak activity
        streakButton.setOnClickListener {
            val intent = Intent(this, StreakActivity::class.java)
            startActivity(intent)
        }

        //Return on the main activity
        buttonComeBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        //Create a new task
        buttonAdd.setOnClickListener {
            if (newTask.text.isNotBlank()){
                val newItem = DailyDbEntity(0, newTask.text.toString().trim(), 0L, LocalDate.now().minusDays(0).toString() ,taskList.size)
                taskList.add(newItem)
                lifecycleScope.launch {
                    taskList[taskList.size-1].id = appRepository.insertNewDailyData(newItem)
                }
                newTask.text.clear()
                //Adapter update
                adapter.notifyItemInserted(taskList.size-1)
                //Completed update
                isCompleted()
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
        var isCompleted = 0
        for (items in list){
            if(items.state == 1L) isCompleted++
        }
        return "$isCompleted/${list.size}"
    }
}