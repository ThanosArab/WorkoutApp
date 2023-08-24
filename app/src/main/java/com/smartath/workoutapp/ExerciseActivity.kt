package com.smartath.workoutapp

import android.app.Dialog

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartath.workoutapp.databinding.ActivityExerciseBinding
import com.smartath.workoutapp.databinding.BackCustomDialogBinding
import java.util.*
import kotlin.collections.ArrayList


class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentPosition = -1

    private var adapter: ExerciseIdAdapter? = null

    private var player: MediaPlayer? = null

    private var textToSpeech: TextToSpeech? = null

    private var timeLeftRest: Long = 0
    private var timeLeftExercise: Long = 0

    private var timeRestTotal: Long = 10000
    private var timeExerciseTotal: Long = 30000

    private var duration = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpToolbar()

        var newTimeRest = intent.getIntExtra("rest",0)
        var newTimeExercise = intent.getIntExtra("exercise", 0)

        if(newTimeRest != 0 && newTimeExercise != 0){
            timeRestTotal = (newTimeRest*1000).toLong()
            timeExerciseTotal = (newTimeExercise*1000).toLong()
        }

        duration = intent.getIntExtra("total_time", 0)

        exerciseList = Constants.defaultExerciseList()

        textToSpeech = TextToSpeech(this, this)

        setUpRestProgress()
        setUpRecyclerView()

        actionButtonsPressed()
    }

    private fun actionButtonsPressed(){
        binding?.pauseBtn?.setOnClickListener {
            pauseExercise()
        }
        binding?.playBtn?.setOnClickListener {
            playExercise()
        }
        binding?.previousBtn?.setOnClickListener {
            previousExercise()
        }
        binding?.nextBtn?.setOnClickListener {
            nextExercise()
        }
    }

    private fun showCustomBackDialog(){
        if (binding?.restLayout?.visibility == View.VISIBLE){
            pauseRest()
        }
        else{
            pauseExercise()
        }

        val dialog = Dialog(this)
        val dialogBinding = BackCustomDialogBinding.inflate(layoutInflater)

        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialogBinding.yesBtn.setOnClickListener {
            this@ExerciseActivity.finish()
            dialog.dismiss()
        }
        dialogBinding.noBtn.setOnClickListener {
            if (binding?.restLayout?.visibility == View.VISIBLE){
                playRest()
            }
            else{
                playExercise()
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setUpToolbar(){
        setSupportActionBar(binding?.exerciseToolbar)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Exercise"
        }
        binding?.exerciseToolbar?.setNavigationOnClickListener {
            showCustomBackDialog()
        }
    }

    private fun pauseRest(){
        restTimer?.cancel()
        binding?.restTimerLinear?.background = ContextCompat.getDrawable(this@ExerciseActivity, R.drawable.item_id_background)
    }


    private fun playRest(){
        restCountDownTimer(timeLeftRest)
        binding?.restTimerLinear?.background = ContextCompat.getDrawable(this@ExerciseActivity, R.drawable.timer_text_layout)
    }

    private fun pauseExercise(){
        exerciseTimer?.cancel()
        binding?.pauseBtn?.background = ContextCompat.getDrawable(this@ExerciseActivity, R.drawable.item_id_selected_background)
        binding?.playBtn?.background = ContextCompat.getDrawable(this@ExerciseActivity, R.drawable.item_button_background)
        binding?.exerciseTimerLinear?.background = ContextCompat.getDrawable(this@ExerciseActivity, R.drawable.item_id_background)
    }

    private fun playExercise(){
        exerciseCountDownTimer(timeLeftExercise)
        binding?.playBtn?.background = ContextCompat.getDrawable(this@ExerciseActivity, R.drawable.item_id_selected_background)
        binding?.pauseBtn?.background = ContextCompat.getDrawable(this@ExerciseActivity, R.drawable.item_button_background)
        binding?.exerciseTimerLinear?.background = ContextCompat.getDrawable(this@ExerciseActivity, R.drawable.timer_text_layout)
    }

    private fun previousExercise(){
        if (currentPosition>0){

            exerciseList!![currentPosition].setIsSelected(false)

            currentPosition -= 1

            exerciseList!![currentPosition].setIsSelected(true)
            exerciseList!![currentPosition].setIsCompleted(false)
            adapter!!.notifyDataSetChanged()

            setUpExerciseProgress()
        }
    }

    private fun nextExercise(){
        if (currentPosition< exerciseList!!.size-1){
            exerciseList!![currentPosition].setIsSelected(false)
            exerciseList!![currentPosition].setIsCompleted(true)

            currentPosition += 1

            exerciseList!![currentPosition].setIsSelected(true)

            adapter!!.notifyDataSetChanged()

            setUpExerciseProgress()
        }
        else{
            finish()
            val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
            intent.putExtra("duration", duration)
            startActivity(intent)
        }
    }

    private fun setUpRecyclerView(){
        binding?.itemIdsRv?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = ExerciseIdAdapter(exerciseList!!)

        binding?.itemIdsRv?.adapter = adapter
    }

    private fun setUpRestProgress(){
        try{
            val soundUri = Uri.parse("android.resource://com.smartath.workoutapp/"+ R.raw.start_audio)
            player = MediaPlayer.create(this@ExerciseActivity, soundUri)
            player?.isLooping = false
            player?.start()
        }
        catch (e: Exception){
            e.printStackTrace()
        }


        binding?.exerciseLayout?.visibility = View.INVISIBLE
        binding?.restLayout?.visibility = View.VISIBLE

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.restExerciseTv?.text = exerciseList!![currentPosition+1].getName()

        restCountDownTimer(timeRestTotal)
    }

    private fun restCountDownTimer(timeLeft:Long){
        binding?.restTimerBar?.progress = restProgress
        binding?.restTimerBar?.max = (timeRestTotal/1000).toInt()

        restTimer = object : CountDownTimer(timeLeft, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.restTimerBar?.progress = (timeRestTotal/1000).toInt() - restProgress
                binding?.restTimerTv?.text = ((timeRestTotal/1000).toInt() - restProgress).toString()
                timeLeftRest = millisUntilFinished-1000
            }
            override fun onFinish() {
                currentPosition++

                exerciseList!![currentPosition].setIsSelected(true)
                adapter!!.notifyDataSetChanged()
                setUpExerciseProgress()
            }
        }.start()
    }

    private fun setUpExerciseProgress(){
        binding?.exerciseLayout?.visibility = View.VISIBLE
        binding?.restLayout?.visibility = View.INVISIBLE

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        binding?.exerciseImage?.setImageResource(exerciseList!![currentPosition].getImage())
        binding?.exerciseTv?.text = exerciseList!![currentPosition].getName()

        speakOut(exerciseList!![currentPosition].getName())

        exerciseCountDownTimer(timeExerciseTotal)
    }

    private fun exerciseCountDownTimer(timeLeft: Long){
        binding?.exerciseTimerBar?.progress = exerciseProgress
        binding?.exerciseTimerBar?.max = (timeExerciseTotal/1000).toInt()

        exerciseTimer = object : CountDownTimer(timeLeft, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.exerciseTimerBar?.progress = (timeExerciseTotal/1000).toInt() - exerciseProgress
                binding?.exerciseTimerTv?.text = ((timeExerciseTotal/1000).toInt() - exerciseProgress).toString()
                timeLeftExercise = millisUntilFinished-1000
            }

            override fun onFinish() {
                if (currentPosition < exerciseList!!.size-1){
                    exerciseList!![currentPosition].setIsSelected(false)
                    exerciseList!![currentPosition].setIsCompleted(true)
                    adapter!!.notifyDataSetChanged()
                    setUpRestProgress()
                }
                else{
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    intent.putExtra("duration", duration)
                    startActivity(intent)
                }
            }
        }.start()
    }

    private fun speakOut(text: String){
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int){
        if (status != TextToSpeech.ERROR){
            val result = textToSpeech?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this@ExerciseActivity, "This language is not supported", Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(this@ExerciseActivity, "Initialization Failed!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        showCustomBackDialog()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (player!= null){
            player?.stop()
        }

        if(textToSpeech != null){
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }

        binding = null
    }
}
