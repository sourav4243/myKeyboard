package com.example.mykeyboard

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout

class KeyboardView(context: Context) : LinearLayout(context) {
    private var keyListener: ((String) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.keyboard_layout, this, true)

        // Map button IDs to their corresponding key values
        val keys = mapOf(
            R.id.key_q to "Q", R.id.key_w to "W", R.id.key_e to "E", R.id.key_r to "R", R.id.key_t to "T",
            R.id.key_y to "Y", R.id.key_u to "U", R.id.key_i to "I", R.id.key_o to "O", R.id.key_p to "P",
            R.id.key_a to "A", R.id.key_s to "S", R.id.key_d to "D", R.id.key_f to "F", R.id.key_g to "G",
            R.id.key_h to "H", R.id.key_j to "J", R.id.key_k to "K", R.id.key_l to "L",
            R.id.key_z to "Z", R.id.key_x to "X", R.id.key_c to "C", R.id.key_v to "V", R.id.key_b to "B",
            R.id.key_n to "N", R.id.key_m to "M",
            R.id.key_space to " ", R.id.key_delete to "DELETE", R.id.key_enter to "\n"
        )

        // Set click listeners for all keys
        keys.forEach { (id, key) ->
            findViewById<Button>(id)?.setOnClickListener {
                keyListener?.invoke(key)
            }
        }
    }

    fun setOnKeyListener(listener: (String) -> Unit) {
        keyListener = listener
    }
    // Adjust the height of the keyboard by overriding the onMeasure method
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Set a custom height (e.g., 300dp)
        val desiredHeight = (250 * resources.displayMetrics.density).toInt()
        val customHeightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, customHeightMeasureSpec)
    }
}
