package com.atwilex.to_do

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        if (!AppDependencies.isInit()) {
            AppDependencies.init(applicationContext)
        }

        //resetSchedule(this)

        //Initialization
        val buttonDaily : Button = findViewById(R.id.daily)
        val buttonDisposable : Button = findViewById(R.id.disposable)
        val buttonGlobal : Button = findViewById(R.id.global)

        //Button to Daily Activity
        buttonDaily.setOnClickListener {
            val intent = Intent(this, DailyActivity::class.java)
            startActivity(intent)
        }

        //Button to Disposable Activity
        buttonDisposable.setOnClickListener {
            val intent = Intent(this, DisposableActivity::class.java)
            startActivity(intent)
        }

        //Button to Global Activity
        buttonGlobal.setOnClickListener {
            val intent = Intent(this, GlobalActivity::class.java)
            startActivity(intent)
        }

        //Something that I can't understand
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}