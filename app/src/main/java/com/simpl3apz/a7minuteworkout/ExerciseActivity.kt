package com.simpl3apz.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.simpl3apz.a7minuteworkout.databinding.ActivityExerciseBinding
import com.simpl3apz.a7minuteworkout.databinding.ActivityMainBinding

class ExerciseActivity : AppCompatActivity() {

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

        // get the exercise list
        exerciseList = Constants.defaultExerciseList()

        // setup the timer in progress bar
        mBinding?.tvRestTimer?.text = (exerciseDuration/1000).toString()
        // set on click actions for the buttons
        // TODO
        setRestView()

    }

    // functions for rest timer operations
    // set rest timer view
    private fun setRestView(){
        // set visibility for the frame layout views for Rest
        mBinding?.flRestView?.visibility = View.VISIBLE
        mBinding?.tvTitleRest?.visibility = View.VISIBLE
        mBinding?.flExerciseView?.visibility = View.INVISIBLE
        mBinding?.tvExerciseName?.visibility = View.INVISIBLE
        mBinding?.ivExercise?.visibility = View.INVISIBLE
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }
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

    // reset viewbinding on exit
    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
    }
}