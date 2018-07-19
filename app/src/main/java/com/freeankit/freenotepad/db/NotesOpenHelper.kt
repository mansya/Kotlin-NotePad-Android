package com.freeankit.freenotepad.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.freeankit.freenotepad.db.NotesOpenHelper.NoteTable.Companion.CREATED_AT
import com.freeankit.freenotepad.db.NotesOpenHelper.NoteTable.Companion.IS_PINNED
import com.freeankit.freenotepad.db.NotesOpenHelper.NoteTable.Companion.TEXT
import com.freeankit.freenotepad.db.NotesOpenHelper.NoteTable.Companion.UPDATED_AT
import com.freeankit.freenotepad.db.NotesOpenHelper.NoteTable.Companion._ID
import com.freeankit.freenotepad.db.NotesOpenHelper.NoteTable.Companion._TABLE_NAME

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class NotesOpenHelper(context: Context) : SQLiteOpenHelper(context, "notes.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    val SQL_CREATE_ENTRIES = """CREATE TABLE $_TABLE_NAME (
        |$_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        |$TEXT TEXT,
        |$IS_PINNED INTEGER,
        |$CREATED_AT INTEGER,
        |$UPDATED_AT INTEGER)"""
            .trimMargin()

    val SQL_DELETE_ENTRIES = """DROP TABLE IF EXISTS $_TABLE_NAME"""

    val SQL_QUERY_ALL = "SELECT * FROM NOTE ORDER BY $CREATED_AT DESC"

    interface NoteTable : BaseColumns {
        companion object {
            val _ID = "_id"
            val _TABLE_NAME = "notes"
            val TEXT = "text"
            val IS_PINNED = "is_pinned"
            val CREATED_AT = "created_at"
            val UPDATED_AT = "updated_at"
        }
    }
}
