package com.example.currencyExchangeApplication.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.conferoapplication.R
import com.google.android.material.snackbar.Snackbar

class SnackBar {

    companion object {
    @SuppressLint("UseRequireInsteadOfGet")
    fun showSnackBar(text: String,view: View, context: Context) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setTextColor(ContextCompat.getColor(context, R.color.white))
            .background(ContextCompat.getColor(context, R.color.red)).show()
    }
      fun Snackbar.background(color: Int): Snackbar {
        this.view.setBackgroundColor(color)
        return this
    }
    }
}