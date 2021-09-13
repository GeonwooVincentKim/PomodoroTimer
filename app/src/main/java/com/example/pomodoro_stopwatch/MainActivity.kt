package com.example.pomodoro_stopwatch

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val remainMinutesTextView: TextView by lazy {
        findViewById<TextView>(R.id.remainMinutesTextView)
    }

    private val remainSecondsTextView: TextView by lazy {
        findViewById<TextView>(R.id.remainSecondsTextView)
    }

    private val seekBar: SeekBar by lazy {
        findViewById<SeekBar>(R.id.seekBar)
    }

    /* If CountDownTimer is null, which mean there is no executing CountDownTimer right now */
    private var currentCountDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        remainMinutesTextView
//        remainSecondsTextView
//        seekBar

        bindViews()
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                /* Does not need to use Suppress-Lint because the value of this function's codes did not hardcoded */
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        updateRemainTime(progress * 60 * 1000L)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    currentCountDownTimer?.cancel()
                    currentCountDownTimer = null
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    /* Mare sure not to work when sets seekBar is not working */
                    seekBar ?: return   // Late - Return
                    currentCountDownTimer =
                        createCountDownTimer(seekBar.progress * 60 * 1000L).start() // Allocates value into currentCountDownTimer
                    currentCountDownTimer?.start()
                }
            }
        )
    }

    private fun createCountDownTimer(initialMills: Long) =
        object : CountDownTimer(initialMills, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                updateRemainTime(0)
                updateSeekBar(0)
            }
        }

    @SuppressLint("SetTextI18n")
    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        remainMinutesTextView.text = "%02d".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMillis: Long) {
        seekBar.progress = (remainMillis / 1000 / 60).toInt()
    }
}
