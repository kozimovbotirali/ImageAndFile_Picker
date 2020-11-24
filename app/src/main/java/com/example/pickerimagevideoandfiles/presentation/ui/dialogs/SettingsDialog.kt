package com.example.pickerimagevideoandfiles.presentation.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.pickerimagevideoandfiles.R
import kotlinx.android.synthetic.main.dialog_settings.*
import kotlinx.android.synthetic.main.dialog_settings.view.*

class SettingsDialog(context: Context, currentCount: Int, currentSize: Float) :
    AlertDialog(context) {
    @SuppressLint("InflateParams")
    private val contentView =
        LayoutInflater.from(context).inflate(R.layout.dialog_settings, null, false)
    private var listener: ((Int, Float) -> Unit)? = null

    init {
        setView(contentView)
        setButton(BUTTON_POSITIVE, "OK") { _, _ -> }
        setButton(BUTTON_NEGATIVE, "Cancel") { _, _ -> dismiss() }

        contentView.apply {
            input_max_count.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        input_layout_max_count.error = null
                        input_layout_max_count.isErrorEnabled = false
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
            input_max_size.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        input_layout_max_size.error = null
                        input_layout_max_size.isErrorEnabled = false
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        setOnShowListener {
            val button = this.getButton(BUTTON_POSITIVE)
            button.setOnClickListener {
                contentView.apply {
                    if (input_max_count.text.isNullOrEmpty()) {
                        input_layout_max_count.error = "Maydonni to'ldiring!"
                        input_max_count.requestFocus()
                        return@setOnClickListener
                    }
                    if (input_max_size.text.isNullOrEmpty()) {
                        input_layout_max_size.error = "Maydonni to'ldiring!"
                        input_max_size.requestFocus()
                        return@setOnClickListener
                    }
                    val count = input_max_count.text.toString().toInt()
                    if (count < currentCount) {
                        input_layout_max_count.error = "Joriy media sonidan ko'proq son kiriting!"
                        input_max_count.requestFocus()
                        return@setOnClickListener
                    }
                    val size = input_max_size.text.toString().toFloat()
                    if (size < currentSize) {
                        input_layout_max_size.error =
                            "Joriy media o'lchamidan ko'proq son kiriting!"
                        input_max_size.requestFocus()
                        return@setOnClickListener
                    }
                    listener?.invoke(count, size)
                    dismiss()
                }
            }
        }
    }

    fun setOnClickListener(block: ((Int, Float) -> Unit)?) {
        listener = block
    }
}