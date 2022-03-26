package com.example.featureflagdebugmenu.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.featureflagdebugmenu.ui.screens.MainActivity

/**
 * Restarts the Activity
 */
fun Context.triggerRestart() {
    val intent = Intent(this, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
    if (this is Activity) {
        this.finish()
    }
}