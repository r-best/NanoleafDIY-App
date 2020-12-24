package com.example.nanoleafdiy.utils

import android.content.Context
import android.widget.Toast

class ToastManager{ companion object {
    lateinit var context: Context
    fun init(context: Context){ this.context = context }
    fun makeText(msg: CharSequence, length: Int){ Toast.makeText(context, msg, length).show() }
}}
