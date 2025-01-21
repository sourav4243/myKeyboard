package com.example.mykeyboard

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout

class KeyboardView(context: Context) : LinearLayout(context) {
    private var keyListener: ((String) -> Unit)? = null
    private var isUpperCase: Boolean = false

    // Cache button references for alphabetic keys
    private val alphabetButtons = mutableMapOf<Int, Button>()
    private val nonAlphabetButtons = mutableListOf<Button>()

    init {
        LayoutInflater.from(context).inflate(R.layout.keyboard_layout, this, true)

        val keys = mapOf(
            R.id.key_q to "q", R.id.key_w to "w", R.id.key_e to "e", R.id.key_r to "r", R.id.key_t to "t",
            R.id.key_y to "y", R.id.key_u to "u", R.id.key_i to "i", R.id.key_o to "o", R.id.key_p to "p",
            R.id.key_a to "a", R.id.key_s to "s", R.id.key_d to "d", R.id.key_f to "f", R.id.key_g to "g",
            R.id.key_h to "h", R.id.key_j to "j", R.id.key_k to "k", R.id.key_l to "l",
            R.id.key_z to "z", R.id.key_x to "x", R.id.key_c to "c", R.id.key_v to "v", R.id.key_b to "b",
            R.id.key_n to "n", R.id.key_m to "m",
            R.id.key_space to " ", R.id.key_delete to "DELETE", R.id.key_enter to "\n"
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
                    keyListener?.invoke(getKeyText(key))
                }
            }
        }

        // Set click listener for the toggle case button
        findViewById<Button>(R.id.key_toggle_case)?.setOnClickListener {
            toggleCase()
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

    fun setOnKeyListener(listener: (String) -> Unit) {
        keyListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = (270 * resources.displayMetrics.density).toInt()
        val customHeightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, customHeightMeasureSpec)
    }
}
