package com.freeankit.freenotepad

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.freeankit.freenotepad.db.DataStore
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import io.fabric.sdk.android.Fabric


/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class NoteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        DataStore.init(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}