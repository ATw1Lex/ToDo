package com.atwilex.to_do

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.atwilex.to_do.AppDependencies.appRepository
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.time.LocalDate

class StreakActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_streak)

        //Initialization
        val left : ImageButton = findViewById(R.id.left)
        val right : ImageButton = findViewById(R.id.right)
        val comeBackButton : ImageButton = findViewById(R.id.comeBack)
        val listView : RecyclerView = findViewById(R.id.streakList)
        val day : TextView = findViewById(R.id.day)
        listView.layoutManager = LinearLayoutManager(this)

        //Binding RecyclerView and Mutable List
        val streakList = mutableListOf<DailyDbEntity>()
        val listAdapter = StreakListAdapter(streakList)
        listView.adapter = listAdapter

        //Init day
        var today = LocalDate.now()
        val updateToday = {day.text = today.toString()}

        //Loading data from database
        lifecycleScope.launch {
            val items = appRepository.getToday(LocalDate.now().toString())
            streakList.clear()
            streakList.addAll(items)
            listAdapter.notifyItemRangeInserted(0, items.size)
        }
        updateToday()

        //Go to yesterday
        left.setOnClickListener {
            today = today.minusDays(1)
            lifecycleScope.launch {
                val items = appRepository.getToday(today.toString())
                val removed = streakList.size
                streakList.clear()
                listAdapter.notifyItemRangeRemoved(0, removed)
                streakList.addAll(items)
                listAdapter.notifyItemRangeInserted(0, items.size)
            }
            updateToday()
        }

        //Go to tomorrow
        right.setOnClickListener {
            today = today.plusDays(1)
            lifecycleScope.launch {
                val items = appRepository.getToday(today.toString())
                val removed = streakList.size
                streakList.clear()
                listAdapter.notifyItemRangeRemoved(0, removed)
                streakList.addAll(items)
                listAdapter.notifyItemRangeInserted(0, items.size)
            }
            updateToday()
        }

        //Return on the daily activity
        comeBackButton.setOnClickListener {
            val intent = Intent(this, DailyActivity::class.java)
            startActivity(intent)
        }

        //Open a dialog, for edit day
        day.setOnClickListener {
            /*try {
                val inflater = LayoutInflater.from(this)
            }catch (e : Exception){
                e.printStackTrace()
            }*/
            Toast.makeText(this, "The functional expected", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}