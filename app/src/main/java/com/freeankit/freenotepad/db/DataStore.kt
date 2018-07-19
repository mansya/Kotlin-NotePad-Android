package com.freeankit.freenotepad.db

import android.content.Context
import org.jetbrains.anko.doAsync

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
object DataStore {
    @JvmStatic
    lateinit var notes: NoteDatabase
        private set

    fun init(context: Context) {
        notes = NoteDatabase(context)
    }

    fun execute(runnable: Runnable) {
        execute { runnable.run() }
    }

    fun execute(fn: () -> Unit) {
        doAsync { fn() }
    }
}
