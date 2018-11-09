package me.bvaleo.issueloader.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class EditTextListener(val et: EditText, val callback: ChangeTextCallback) : TextWatcher {
    override fun afterTextChanged(text: Editable?) = callback.onTextChanged(et.text.toString())

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}

interface ChangeTextCallback {
    fun onTextChanged(repo: String)
}