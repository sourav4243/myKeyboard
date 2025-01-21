package com.example.mykeyboard

import android.content.Context
import android.view.LayoutInflater
//import android.os.Vibrator
//import android.os.VibrationEffect
import android.widget.Button
import android.widget.LinearLayout

class KeyboardView(context: Context) : LinearLayout(context) {
    private var keyListener: ((String) -> Unit)? = null
    private var isUpperCase: Boolean = false  // To track if the keyboard is in uppercase mode
//    private var vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    // Store original text for keys
    private val originalKeyTexts = mutableMapOf<Int, String>()

    init {
        LayoutInflater.from(context).inflate(R.layout.keyboard_layout, this, true)

        // Map button IDs to their corresponding key values
        val keys = mapOf(
            R.id.key_q to "q", R.id.key_w to "w", R.id.key_e to "e", R.id.key_r to "r", R.id.key_t to "t",
            R.id.key_y to "y", R.id.key_u to "u", R.id.key_i to "i", R.id.key_o to "o", R.id.key_p to "p",
            R.id.key_a to "a", R.id.key_s to "s", R.id.key_d to "d", R.id.key_f to "f", R.id.key_g to "g",
            R.id.key_h to "h", R.id.key_j to "j", R.id.key_k to "k", R.id.key_l to "l",
            R.id.key_z to "z", R.id.key_x to "x", R.id.key_c to "c", R.id.key_v to "v", R.id.key_b to "b",
            R.id.key_n to "n", R.id.key_m to "m",
            R.id.key_space to " ", R.id.key_delete to "DELETE", R.id.key_enter to "\n"
        )
        // Set click listeners for all keys
        keys.forEach { (id, key) ->
            val button = findViewById<Button>(id)
            button?.setOnClickListener {
                keyListener?.invoke(getKeyText(key))
//                vibrateKeyPress()
            }
            originalKeyTexts[id] = key  // Store original lowercase key text
        }

        // Set click listener for the toggle case button (e.g., "A/a")
        val toggleCaseButton = findViewById<Button>(R.id.key_toggle_case)
        toggleCaseButton?.setOnClickListener {
            toggleCase()
//            vibrateKeyPress()
        }
    }

    // Toggle the case between uppercase and lowercase
    private fun toggleCase() {
        isUpperCase = !isUpperCase

        // Update the button text to reflect the case
        val toggleCaseButton = findViewById<Button>(R.id.key_toggle_case)
        toggleCaseButton?.text = if (isUpperCase) "a/A" else "A/a"

        // Update only alphabetic key texts
        updateAlphabeticKeyText()
    }

    // Update the text of only the alphabetic keys
    private fun updateAlphabeticKeyText() {
        val alphabetKeys = listOf(
            R.id.key_q, R.id.key_w, R.id.key_e, R.id.key_r, R.id.key_t, R.id.key_y, R.id.key_u, R.id.key_i,
            R.id.key_o, R.id.key_p, R.id.key_a, R.id.key_s, R.id.key_d, R.id.key_f, R.id.key_g, R.id.key_h,
            R.id.key_j, R.id.key_k, R.id.key_l, R.id.key_z, R.id.key_x, R.id.key_c, R.id.key_v, R.id.key_b,
            R.id.key_n, R.id.key_m
        )

        alphabetKeys.forEach { id ->
            val button = findViewById<Button>(id)
            val originalText = originalKeyTexts[id]
            button?.text = if (isUpperCase) originalText?.toUpperCase() else originalText?.toLowerCase()
        }
    }

    // Get the key text based on the case
    private fun getKeyText(key: String): String {
        // Special handling for the "DELETE" key
        return when (key) {
            "DELETE" -> key  // Do not modify "DELETE" key text
            else -> if (isUpperCase) key.toUpperCase() else key.toLowerCase()
        }
    }

    // Set listener for key presses
    fun setOnKeyListener(listener: (String) -> Unit) {
        keyListener = listener
    }

    // Adjust the height of the keyboard by overriding the onMeasure method
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = (270 * resources.displayMetrics.density).toInt()
        val customHeightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, customHeightMeasureSpec)
    }
//    private fun vibrateKeyPress(){
//        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
//            vibrator.vibrate(VibrationEffect.createOneShot(30,VibrationEffect.DEFAULT_AMPLITUDE))
//        }else{
//            vibrator.vibrate(30)
//        }
//    }
}
