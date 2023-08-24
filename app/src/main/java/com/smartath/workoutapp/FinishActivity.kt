package com.smartath.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.smartath.workoutapp.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {

    private var binding: ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpToolbar()

        binding?.finishBtn?.setOnClickListener {
            finish()
        }

        val dao = (application as WorkOutApp).db.historyDao()
        addEntryToDatabase(dao)
    }

    private fun setUpToolbar(){
        setSupportActionBar(binding?.toolbarFinish)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "FINISH"
        }

        binding?.toolbarFinish?.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    private fun addEntryToDatabase(historyDao: HistoryDao){
        val calendar = Calendar.getInstance()
        val time = calendar.time

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(time)

        val intent = intent.getIntExtra("duration", 0)

        val duration = "$intent secs"

        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date = date, duration = duration))
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

}