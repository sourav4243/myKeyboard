package com.example.mykeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputConnection
import com.example.mykeyboard.KeyboardView

class KeyboardService : InputMethodService() {
    override fun onCreateInputView(): View {
        val keyboardView = KeyboardView(this)
        keyboardView.setOnKeyListener { key ->
            val inputConnection: InputConnection = currentInputConnection ?: return@setOnKeyListener
            when (key) {
                "DELETE" -> inputConnection.deleteSurroundingText(1, 0)
                "ENTER" -> inputConnection.commitText("\n", 1)
                else -> inputConnection.commitText(key, 1)
            }
        }
        return keyboardView
    }
}
