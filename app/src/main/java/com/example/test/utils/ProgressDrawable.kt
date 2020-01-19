package com.example.test.utils

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

class ProgressDrawable {

    companion object{
        fun getProgressDrawable(context: Context): CircularProgressDrawable {
            return CircularProgressDrawable(context).apply {
                strokeWidth = 5f
                centerRadius= 30f
                start()
            }
        }
    }

}