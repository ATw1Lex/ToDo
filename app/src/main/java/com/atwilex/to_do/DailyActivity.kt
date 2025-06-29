package com.atwilex.to_do

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.atwilex.to_do.AppDependencies.appRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily)

        //Initialization activity's elements
        val buttonComeBack : ImageButton = findViewById(R.id.comeBack)
        val buttonAdd : Button = findViewById(R.id.add)
        val newTask : EditText = findViewById(R.id.NewTask)
        val taskList : ListView = findViewById(R.id.ListDailyTasks)
        val list = mutableListOf<DailyDbEntity>()
        val complete : TextView = findViewById(R.id.complete)
        val adapter = DailyListAdapter(this, list, appRepository) {
            complete.text = isCompletedTasks(list)
        }
        taskList.adapter = adapter

        //Update list, with values from database
        CoroutineScope(Dispatchers.Main).launch {
            val items = appRepository.getDailyTab()
            list.clear()
            list.addAll(items)
            adapter.notifyDataSetChanged()
            complete.text = isCompletedTasks(list)
        }

        //Return on the last activity
        buttonComeBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Create new task
        buttonAdd.setOnClickListener {
            if (newTask.text.isNotBlank()){
                CoroutineScope(Dispatchers.Main).launch {
                    val newItem = DailyDbEntity(0, newTask.text.toString(), 0L)
                    val id = appRepository.insertNewDailyData(newItem)
                    list.add(newItem.copy(id = id))
                    newTask.setText("")
                    complete.text = isCompletedTasks(list)
                    adapter.notifyDataSetChanged()
                }
                Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show()
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