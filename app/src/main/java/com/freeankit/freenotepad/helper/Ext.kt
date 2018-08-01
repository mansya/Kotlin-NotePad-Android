package com.freeankit.freenotepad.helper

import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.freeankit.freenotepad.R
import org.jetbrains.anko.contentView

/**
 * @author Ankit Kumar on 31/07/2018
 */


fun Activity.showSnackbar(message: String) {
    val sb = Snackbar.make(findViewById<View>(android.R.id.content), message, Snackbar.LENGTH_SHORT)
    val sbView = sb.view
    sbView.setBackgroundColor(applicationContext?.resources?.getColor(R.color.colorAccent) as Int)
    val textView = sbView.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
    textView.setTextColor(applicationContext?.resources?.getColor(R.color.white) as Int)
    sb.show()

}

fun Activity.hideKeyboard() {
    val imm: InputMethodManager = getSystemService(
            Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(contentView?.windowToken, 0)
}

fun Activity.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}