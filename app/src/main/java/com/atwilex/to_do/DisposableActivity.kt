package com.atwilex.to_do

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.atwilex.to_do.AppDependencies.appRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DisposableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_disposable)

        //Initialization activity's elements
        val buttonComeBack : ImageButton = findViewById(R.id.comeBack)
        val buttonAdd : Button = findViewById(R.id.add)
        val newTask : EditText = findViewById(R.id.NewTask)

        //Initialization List with adapter
        val taskList : ListView = findViewById(R.id.ListDisposableTasks)
        val list = mutableListOf<DisposableDbEntity>()
        val adapter = DisposableListAdapter(this, list, appRepository)
        taskList.adapter = adapter

        //Update list with values from database
        CoroutineScope(Dispatchers.Main).launch {
            val items = appRepository.getDisposableTab()
            list.clear()
            list.addAll(items)
            adapter.notifyDataSetChanged()
        }

        //Return on the last activity
        buttonComeBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Create a new task
        buttonAdd.setOnClickListener {
            if(newTask.text.isNotBlank()){
                CoroutineScope(Dispatchers.Main).launch {
                    val newItem = DisposableDbEntity(0, newTask.text.toString())
                    val id = appRepository.insertNewDisposableData(newItem)
                    list.add(newItem.copy(id = id))
                    newTask.text.clear()
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
}