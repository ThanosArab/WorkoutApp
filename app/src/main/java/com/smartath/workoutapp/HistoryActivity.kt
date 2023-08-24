package com.smartath.workoutapp

import android.app.AlertDialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartath.workoutapp.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpToolbar()

        val dao = (application as WorkOutApp).db.historyDao()

        lifecycleScope.launch {
            dao.fetchAllDates().collect{
                val list = ArrayList(it)
                setUpRecyclerView(list, dao)
            }
        }

    }

    private fun setUpToolbar(){
        setSupportActionBar(binding?.historyToolbar)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }

        binding?.historyToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpRecyclerView(historyList: ArrayList<HistoryEntity>, historyDao: HistoryDao){
        if(historyList.isNotEmpty()){
            val historyAdapter = HistoryAdapter(historyList) { deleteId ->
                deleteExerciseDialog(deleteId, historyDao)
            }
            binding?.itemsRv?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding?.itemsRv?.adapter = historyAdapter
            binding?.itemsRv?.visibility = View.VISIBLE

            binding?.messageTv?.visibility = View.INVISIBLE
        }
        else{
            binding?.itemsRv?.visibility = View.INVISIBLE

            binding?.messageTv?.visibility = View.VISIBLE
        }
    }

    private fun deleteExerciseDialog(deleteId: Int, historyDao: HistoryDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Exercise")
        builder.setIcon(R.drawable.ic_alert)

        builder.setPositiveButton("Yes"){ dialogInterface, _ ->
            lifecycleScope.launch {
                historyDao.delete(HistoryEntity(deleteId))
                Toast.makeText(this@HistoryActivity, "Exercise deleted successfully!", Toast.LENGTH_LONG).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}