package com.freeankit.freenotepad

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.freeankit.freenotepad.db.DataStore
import io.fabric.sdk.android.Fabric



/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class NoteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        DataStore.init(this)
    }
}