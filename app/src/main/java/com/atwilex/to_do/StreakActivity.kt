package com.atwilex.to_do

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
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
            val items = appRepository.getAnyDailyTasks(LocalDate.now().toString())
            streakList.clear()
            streakList.addAll(items)
            listAdapter.notifyItemRangeInserted(0, items.size)
        }
        updateToday()

        //Go to yesterday
        left.setOnClickListener {
            today = today.minusDays(1)
            lifecycleScope.launch {
                val items = appRepository.getAnyDailyTasks(today.toString())
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
                val items = appRepository.getAnyDailyTasks(today.toString())
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
            try {
                //Create today by calendar
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                //Create listener, load task by day
                val dateSetListener = DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                    today = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                    updateToday()
                    lifecycleScope.launch {
                        val items = appRepository.getAnyDailyTasks(today.toString())
                        val removedRange = streakList.size
                        streakList.clear()
                        listAdapter.notifyItemRangeRemoved(0, removedRange)
                        streakList.addAll(items)
                        listAdapter.notifyItemRangeInserted(0, items.size)
                    }
                }

                //Create dialog by context, style, listener, date
                val datePickerDialog = DatePickerDialog(this, R.style.SpinnerDatePickerDialogTheme, dateSetListener, year, month, day)

                //Create min date that user can select
                val minDate = Calendar.getInstance()
                minDate.set(2000, 0, 1)

                //Set minDate
                datePickerDialog.datePicker.minDate = minDate.timeInMillis

                //Show dialog to user
                datePickerDialog.show()
            }catch (e : Exception){
                e.printStackTrace()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}