package com.smartath.workoutapp

import android.app.Dialog
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.smartath.workoutapp.databinding.ActivityMainBinding
import com.smartath.workoutapp.databinding.DurationCustomDialogBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private var newTimeRest: Int = 0
    private var newTimeExercise: Int = 0

    private var duration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setGradientText()

        setDuration()

        startExercise()
        startBMICalculator()
        startHistory()
    }

    private fun setGradientText(){
        binding?.logoTv?.setTextColor(resources.getColor(R.color.blue_5))

        val paint = binding?.logoTv?.paint
        val width = paint?.measureText(binding?.logoTv?.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width!!, binding?.logoTv?.textSize!!, intArrayOf(
            ContextCompat.getColor(this, R.color.blue_4),
            ContextCompat.getColor(this, R.color.blue_1)
        ), null, TileMode.REPEAT)

        binding?.logoTv?.paint?.shader = textShader
    }

    private fun startExercise(){
        binding?.startLayout?.setOnClickListener {

            if(duration == 0){
                duration = 12*10 + 12*30
            }

            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtra("rest", newTimeRest)
            intent.putExtra("exercise", newTimeExercise)
            intent.putExtra("total_time", duration)
            startActivity(intent)
        }
    }

    private fun setDuration(){
        binding?.durationLayout?.setOnClickListener {
            val dialog = Dialog(this, R.style.Theme_Dialog)
            val dialogBinding = DurationCustomDialogBinding.inflate(layoutInflater)

            dialog.setContentView(dialogBinding.root)
            dialogBinding.saveBtn.setOnClickListener {
                if (dialogBinding.restEt.text.isNotEmpty() && dialogBinding.exerciseEt.text.isNotEmpty()){
                    val rest = dialogBinding.restEt.text.toString()
                    newTimeRest = rest.toInt()

                    val exercise = dialogBinding.exerciseEt.text.toString()
                    newTimeExercise = exercise.toInt()

                    duration = newTimeRest*12 + newTimeExercise*12

                    dialog.dismiss()
                }
                else if (dialogBinding.restEt.text.equals("0") && dialogBinding.exerciseEt.text.equals("0")){
                    Toast.makeText(this@MainActivity, "Not accepted duration.", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@MainActivity, "Please enter the preferred Rest and Exercise duration.", Toast.LENGTH_LONG).show()
                }
            }
            dialogBinding.cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun startBMICalculator(){
        binding?.bmiLayout?.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startHistory(){
        binding?.historyLayout?.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

}