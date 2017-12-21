package com.freeankit.freenotepad

import java.util.Date

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
data class Note(var id: Int = -1,
                var text: String? = null,
                var isPinned: Boolean = false,
                var createdAt: Date = Date(),
                var updatedAt: Date? = null)
