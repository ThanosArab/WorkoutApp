package com.smartath.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.smartath.workoutapp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_VIEW = "METRIC_VIEW"
        private const val US_VIEW = "US_VIEW"
    }

    private var binding: ActivityBmiBinding? = null

    private var currentVisibleView: String = METRIC_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpToolbar()

        makeVisibleMetricUnitsView()

        binding?.unitsRg?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.metricsRb){
                makeVisibleMetricUnitsView()
            }
            else{
                makeVisibleUSUnitsView()
            }
        }

        binding?.calculateBtn?.setOnClickListener {
            calculateUnits()
        }
    }

    private fun setUpToolbar(){
        setSupportActionBar(binding?.bmiToolbar)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "BMI Calculation"
        }

        binding?.bmiToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_VIEW
        binding?.weightLayout?.visibility = View.VISIBLE
        binding?.heightLayout?.visibility = View.VISIBLE

        binding?.weightUSLayout?.visibility = View.INVISIBLE
        binding?.feetHeightLayout?.visibility = View.INVISIBLE
        binding?.inchHeightLayout?.visibility = View.INVISIBLE

        binding?.weightEt?.text!!.clear()
        binding?.heightEt?.text!!.clear()

        binding?.bmiDisplayLayout?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_VIEW
        binding?.weightLayout?.visibility = View.INVISIBLE
        binding?.heightLayout?.visibility = View.INVISIBLE

        binding?.weightUSLayout?.visibility = View.VISIBLE
        binding?.feetHeightLayout?.visibility = View.VISIBLE
        binding?.inchHeightLayout?.visibility = View.VISIBLE

        binding?.weightUSEt?.text!!.clear()
        binding?.feetHeightEt?.text!!.clear()
        binding?.inchHeightEt?.text!!.clear()

        binding?.bmiDisplayLayout?.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float){

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }
        else if (bmi.compareTo(16f) <=0 && bmi.compareTo(15f) >0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }
        else if (bmi.compareTo(18.5f) <=0 && bmi.compareTo(16f) >0){
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }
        else if (bmi.compareTo(25f) <=0 && bmi.compareTo(18.5f) >0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in good shape!"
        }
        else if (bmi.compareTo(30f) <=0 && bmi.compareTo(25f) >0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout more!"
        }
        else if (bmi.compareTo(35f) <=0 && bmi.compareTo(30f) >0){
            bmiLabel = "Obese Class | (Moderately Obese)"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout more!"
        }
        else if (bmi.compareTo(40f) <=0 && bmi.compareTo(35f) >0){
            bmiLabel = "Obese Class || (Severely Obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        else{
            bmiLabel = "Obese Class ||| (Very Severely Obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.bmiDisplayLayout?.visibility = View.VISIBLE
        binding?.bmiValueTv?.text = bmiValue
        binding?.bmiTypeTv?.text = bmiLabel
        binding?.bmiDescriptionTv?.text = bmiDescription
    }

    private fun validateMetricUnits():Boolean{
        var isValid = true

        if (binding?.weightEt?.text.toString().isEmpty()){
            isValid = false
        }
        else if(binding?.heightEt?.text.toString().isEmpty()){
            isValid = false
        }

        return isValid
    }

    private fun validateUSUnits():Boolean{
        var isValid = true

        if (binding?.weightUSEt?.text.toString().isEmpty()){
            isValid = false
        }
        else if(binding?.feetHeightEt?.text.toString().isEmpty()){
            isValid = false
        }
        else if(binding?.inchHeightEt?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun calculateUnits(){
        if (currentVisibleView == METRIC_VIEW){
            if (validateMetricUnits()){
                val heightValue: Float = binding?.heightEt?.text.toString().toFloat() / 100
                val weightValue: Float = binding?.weightEt?.text.toString().toFloat()

                val bmi = weightValue / (heightValue*heightValue)

                displayBMIResult(bmi)
            }
            else{
                Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_LONG).show()
            }
        }
        else{
            if(validateUSUnits()){
                val feetUsValue: Float = binding?.feetHeightEt?.text.toString().toFloat()
                val inchUsValue: Float = binding?.inchHeightEt?.text.toString().toFloat()
                val weightUSValue: Float = binding?.weightUSEt?.text.toString().toFloat()

                val heightValue = inchUsValue + feetUsValue*12

                val bmi = 703*(weightUSValue / (heightValue*heightValue))

                displayBMIResult(bmi)
            }
            else{
                Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}
