package com.atwilex.to_do

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.atwilex.to_do.AppDependencies.appRepository
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class StreakActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_streak)

        //Initialization
        val comeBackButton : ImageButton = findViewById(R.id.comeBack)
        val listView : RecyclerView = findViewById(R.id.streakList)
        val day : TextView = findViewById(R.id.day)
        listView.layoutManager = LinearLayoutManager(this)

        //Binding RecyclerView and Mutable List
        val streakList = mutableListOf<DailyDbEntity>()
        val listAdapter = StreakListAdapter(streakList)
        listView.adapter = listAdapter

        //Init day
        day.text = "14 October 2025"

        //Loading data from database
        lifecycleScope.launch {
            val items = appRepository.getDailyTab()
            streakList.clear()
            streakList.addAll(items)
            listAdapter.notifyItemRangeInserted(0, items.size)
        }

        //Return on the daily activity
        comeBackButton.setOnClickListener {
            val intent = Intent(this, DailyActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}