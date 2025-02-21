package com.example.mykeyboard

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.media.AudioAttributes
import android.media.SoundPool

import android.os.Handler
import android.os.Looper
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputConnection
import androidx.appcompat.app.AppCompatActivity

class KeyboardView(context: Context) : LinearLayout(context) {
    private var keyListener: ((String) -> Unit)? = null
    private var isUpperCase: Boolean = false
//    private var soundId: Int = 0
//    private var soundPool: SoundPool? = null
//    private var soundVolume: Float = 1.0f // Default volume
    private var deleteHandler: Handler = Handler()
    private var deleteRunnable: Runnable? = null
    private var inputConnection: InputConnection? = null

    // Cache button references for alphabetic keys
    private val alphabetButtons = mutableMapOf<Int, Button>()
    private val nonAlphabetButtons = mutableListOf<Button>()

    init {
        LayoutInflater.from(context).inflate(R.layout.keyboard_layout, this, true)

        // Initialize SoundPool with increased max streams for better performance
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .build()

//        soundPool = SoundPool.Builder()
//            .setMaxStreams(50) // Increased to allow multiple sounds
//            .setAudioAttributes(audioAttributes)
//            .build()
//
//        // Load the sound file
//        soundId = soundPool?.load(context, R.raw.click_sound, 1) ?: 0

        val keys = mapOf(
            R.id.key_q to "q", R.id.key_w to "w", R.id.key_e to "e", R.id.key_r to "r", R.id.key_t to "t",
            R.id.key_y to "y", R.id.key_u to "u", R.id.key_i to "i", R.id.key_o to "o", R.id.key_p to "p",
            R.id.key_a to "a", R.id.key_s to "s", R.id.key_d to "d", R.id.key_f to "f", R.id.key_g to "g",
            R.id.key_h to "h", R.id.key_j to "j", R.id.key_k to "k", R.id.key_l to "l",
            R.id.key_z to "z", R.id.key_x to "x", R.id.key_c to "c", R.id.key_v to "v", R.id.key_b to "b",
            R.id.key_n to "n", R.id.key_m to "m",
            R.id.key_space to " ", R.id.key_delete to "DELETE", R.id.key_enter to "\n", R.id.key_question_mark to "?",
            R.id.key_full_stop to ".", R.id.suggested_word1 to "Suggested 1", R.id.suggested_word2 to "Suggested 2",
            R.id.suggested_word3 to "Suggested 3", R.id.key_1 to "1", R.id.key_2 to "2", R.id.key_3 to "3",
            R.id.key_4 to "4", R.id.key_5 to "5", R.id.key_6 to "6", R.id.key_7 to "7", R.id.key_8 to "8",
            R.id.key_9 to "9", R.id.key_0 to "0", R.id.key_apostrophe to "'"
        )

        // Cache button references and set click listeners
        keys.forEach { (id, key) ->
            val button = findViewById<Button>(id)
            button?.let {
                if (key.length == 1 && key[0].isLetter()) {
                    alphabetButtons[id] = it
                } else {
                    nonAlphabetButtons.add(it)
                }
                it.setOnClickListener {
//                    playKeyPressSound() // Play sound on key press
                    keyListener?.invoke(getKeyText(key))
                }

                // Handle long press for DELETE Key
                if(key=="DELETE"){
                    it.setOnTouchListener{_, event ->
                        when(event.action){
                            MotionEvent.ACTION_DOWN -> startDeletingfnc()
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> stopDeletingfnc()
                        }
                        false
                    }
                }
            }
        }

        // Set click listener for the toggle case button
        findViewById<Button>(R.id.key_toggle_case)?.setOnClickListener {
            toggleCase()
//            playKeyPressSound() // Optional: Play sound on toggle case
        }
    }

    private fun toggleCase() {
        isUpperCase = !isUpperCase

        // Update text for alphabetic keys only
        alphabetButtons.values.forEach { button ->
            button.text = if (isUpperCase) button.text.toString().uppercase() else button.text.toString().lowercase()
        }
    }

    private fun getKeyText(key: String): String {
        return if (key == "DELETE" || key == " " || key == "\n") {
            key
        } else {
            if (isUpperCase) key.uppercase() else key.lowercase()
        }
    }

    // Play the tap sound with the current volume level
//    private fun playKeyPressSound() {
//        soundPool?.play(soundId, soundVolume, soundVolume, 1, 0, 1f)
//    }

    // Set the listener for key presses
    fun setOnKeyListener(listener: (String) -> Unit) {
        keyListener = listener
    }

    // Set the sound volume dynamically
//    fun setSoundVolume(newVolume: Float) {
//        soundVolume = newVolume.coerceIn(0f, 1f) // Clamp volume to range [0.0, 1.0]
//    }
//
//    // Release SoundPool resources
//    fun releaseSoundPool() {
//        soundPool?.release()
//        soundPool = null
//    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = (355 * resources.displayMetrics.density).toInt()
        val customHeightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, customHeightMeasureSpec)
    }
    private fun startDeletingfnc(){
        deleteRunnable = Runnable {
            inputConnection?.deleteSurroundingText(1,0)
            deleteHandler.postDelayed(deleteRunnable!!, 80) // speed up deletion
        }
        deleteHandler.postDelayed(deleteRunnable!!, 400)    // delay before start
    }

    private fun stopDeletingfnc(){
        deleteHandler.removeCallbacks(deleteRunnable!!)
    }
}
