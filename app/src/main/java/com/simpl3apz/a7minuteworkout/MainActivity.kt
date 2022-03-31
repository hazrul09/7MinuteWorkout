package com.simpl3apz.a7minuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.simpl3apz.a7minuteworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    // set viewbinding object
    private var mBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate viewbinding object
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        // set content to viewbinding object
        setContentView(mBinding?.root)

        // set on click action for fl_start
        mBinding?.flStart?.setOnClickListener {
            var mIntent = Intent(this,ExerciseActivity::class.java)
            startActivity(mIntent)
        }

    }

    // reset viewbinding on exit
    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}