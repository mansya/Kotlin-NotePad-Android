package com.freeankit.freenotepad

import android.app.Application

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class NoteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DataStore.init(this)
    }
}