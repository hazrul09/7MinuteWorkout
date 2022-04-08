package com.simpl3apz.a7minuteworkout

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.simpl3apz.a7minuteworkout.databinding.ActivityExerciseBinding
import com.simpl3apz.a7minuteworkout.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : TextToSpeech.OnInitListener, AppCompatActivity() {

    private val TAG = "ExerciseActivity"
    // set viewbinding object
    private var mBinding: ActivityExerciseBinding? = null
    // variable for the countdown timer object
    private var exerciseTimer : CountDownTimer? = null
    private var restTimer : CountDownTimer? = null
    private var exerciseProgress = 0
    private var restProgress = 0
    // timer duration
    private var exerciseDuration : Long = 30000
    private var restDuration : Long = 5000
    // pauseOffset = timerDuration - timeLeft
    private var exercisePauseOffset : Long = 0
    private var restPauseOffset : Long = 0
    // array for exercises
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    // text to speech object
    private var tts : TextToSpeech? = null
    // media player object
    private var mBeepShort : MediaPlayer? = null
    private var mBeepLong : MediaPlayer? = null
    // exercise status adapter
    private var exerciseStatAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate viewbinding object
        mBinding = ActivityExerciseBinding.inflate(layoutInflater)
        // set content to viewbinding object
        setContentView(mBinding?.root)

        // toolbar setup
        setSupportActionBar(mBinding?.tbExercise)
        if(supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        mBinding?.tbExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        // setup for tts
        tts = TextToSpeech(this, this)

        // setup for the beep sounds
        try {
            val beepLongUri = Uri.parse("android.resource://com.simpl3apz.a7minuteworkout/" + R.raw.beep_long)
            mBeepLong = MediaPlayer.create(applicationContext, beepLongUri)
            mBeepLong?.isLooping = false
           } catch (e: Exception){
            e.printStackTrace()
        }
        try {
            val beepShortUri = Uri.parse("android.resource://com.simpl3apz.a7minuteworkout/" + R.raw.beep_short)
            mBeepShort = MediaPlayer.create(applicationContext, beepShortUri)
            mBeepShort?.isLooping = false
        } catch (e: Exception){
            e.printStackTrace()
        }

        // get the exercise list
        exerciseList = Constants.defaultExerciseList()

        // setup the exercise status recycler view
        setupExerciseRv()

        // start the exercise with initial rest view
        setRestView()

    }

    // function to setup exercise status recycler view
    private fun setupExerciseRv(){
        exerciseStatAdapter = ExerciseStatusAdapter(exerciseList!!)
        mBinding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        mBinding?.rvExerciseStatus?.adapter = exerciseStatAdapter
    }

    // functions for rest timer operations
    // set rest timer view
    private fun setRestView(){
        // set visibility for the frame layout views for Rest
        mBinding?.flRestView?.visibility = View.VISIBLE
        mBinding?.tvTitleRest?.visibility = View.VISIBLE
        mBinding?.tvTitleNextExercise?.visibility = View.VISIBLE
        mBinding?.tvNextExercise?.visibility = View.VISIBLE
        mBinding?.flExerciseView?.visibility = View.INVISIBLE
        mBinding?.tvExerciseName?.visibility = View.INVISIBLE
        mBinding?.ivExercise?.visibility = View.INVISIBLE
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }
        // set the title for next exercise
        mBinding?.tvNextExercise?.text = exerciseList!![currentExercisePosition+1].getName()
        setRestProgressbar()
    }
    // set rest progress bar
    private fun setRestProgressbar(){
        mBinding?.pbRest?.progress = restProgress
        restTimer = object : CountDownTimer(10000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                mBinding?.pbRest?.progress = 10 - restProgress
                mBinding?.tvRestTimer?.text = (10-restProgress).toString()
                // tts next exercise
                if(restProgress==2){
                    // tts next exercise
                    speakText("next exercise, " + exerciseList!![currentExercisePosition+1].getName())
                }
                // play short beep
                if(restProgress in arrayOf(8,9)){
                    mBeepShort?.start()
                }
                if(restProgress==10){
                    mBeepLong?.start()
                }
            }
            override fun onFinish() {
                currentExercisePosition++
                setExerciseView()
            }
        }
        restTimer?.start()
    }

    // functions for exercise timer operations
    // set rest timer view
    private fun setExerciseView(){
        // set visibility for the frame layout views for Exercise
        mBinding?.flRestView?.visibility = View.INVISIBLE
        mBinding?.tvTitleRest?.visibility = View.INVISIBLE
        mBinding?.tvTitleNextExercise?.visibility = View.INVISIBLE
        mBinding?.tvNextExercise?.visibility = View.INVISIBLE
        mBinding?.flExerciseView?.visibility = View.VISIBLE
        mBinding?.tvExerciseName?.visibility = View.VISIBLE
        mBinding?.ivExercise?.visibility = View.VISIBLE
        if(exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        // set the exercise image and name
        mBinding?.ivExercise?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        mBinding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        // tts for start of exercise
        speakText("go")
        // set and start the progress bar for exercise
        setExerciseProgressbar()
    }
    // set exercise progress bar
    private fun setExerciseProgressbar(){
        mBinding?.pbExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(30000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                mBinding?.pbExercise?.progress = 30 - exerciseProgress
                mBinding?.tvExerciseTimer?.text = (30-exerciseProgress).toString()
                // play short beep
                if(exerciseProgress in arrayOf(28,29)){
                    mBeepShort?.start()
                }
                // play long beep
                if(exerciseProgress==30){
                    mBeepLong?.start()
                }
            }
            override fun onFinish() {
                if(currentExercisePosition < exerciseList!!.size - 1){
                    setRestView()
                } else {
                    Toast.makeText(applicationContext,"Congratulation! You have completed the workout.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        exerciseTimer?.start()
    }

    private fun speakText(text : String){
        tts?.speak(text,TextToSpeech.QUEUE_ADD, null, "")
    }

    // reset viewbinding on exit
    override fun onDestroy() {
        super.onDestroy()
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        // media player stop
        if(mBeepLong!=null){
            mBeepLong?.stop()
        }
        if(mBeepShort!=null){
            mBeepShort?.stop()
        }
        // stop tts and shut it down
        if(tts!=null){
            tts?.stop()
            tts?.shutdown()
        }
        mBinding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.ENGLISH)
            Log.i("TTS", "TTS initialization success")
            if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS","Language not supported" )
            }
        } else {
            Log.e("TTS","TTS initialization failed")
        }
    }
}