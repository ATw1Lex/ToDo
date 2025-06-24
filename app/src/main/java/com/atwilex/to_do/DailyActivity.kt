package com.atwilex.to_do

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.atwilex.to_do.DependenciesDaily.dailyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily)

        val buttonComeBack : ImageButton = findViewById(R.id.comeBack)
        val buttonAdd : Button = findViewById(R.id.add)
        val newTask : EditText = findViewById(R.id.NewTask)

        val taskList : ListView = findViewById(R.id.ListDailyTasks)
        val list = mutableListOf<DailyDbEntity>()
        val complete : TextView = findViewById(R.id.complete)
        val adapter = MyListAdapter(this, list, dailyRepository) {
            complete.text = isCompleteTasks(list)
        }
        taskList.adapter = adapter

        CoroutineScope(Dispatchers.Main).launch {
            val items = dailyRepository.getDailyTab()
            list.clear()
            list.addAll(items)
            adapter.notifyDataSetChanged()
            complete.text = isCompleteTasks(list)
        }

        buttonComeBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonAdd.setOnClickListener {
            if (newTask.text.isNotBlank()){
                CoroutineScope(Dispatchers.Main).launch {
                    val newItem = DailyDbEntity(0, newTask.text.toString(), 0L)
                    val id = dailyRepository.insertNewData(newItem)
                    list.add(newItem.copy(id = id))
                    newTask.setText("")
                    complete.text = isCompleteTasks(list)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun isCompleteTasks(list : List<DailyDbEntity>): String{
        var isComplete = 0
        for (items in list){
            if(items.state == 1L) isComplete++
        }
        return "$isComplete/${list.size}"
    }
}