package com.freeankit.freenotepad

import android.content.Context

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
object DataStore {

    val EXEC: Executor = Executors.newSingleThreadExecutor()

    var notes: NoteDatabase? = null
        private set

    fun init(context: Context) {
        notes = NoteDatabase(context)
    }

    fun execute(runnable: () -> Boolean) {
        EXEC.execute(runnable)
    }
}
