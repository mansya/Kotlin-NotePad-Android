package com.freeankit.freenotepad

import java.util.*

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class Note {
    var id = -1
    var text: String? = null
    var isPinned = false
    var createdAt = Date()
    var updatedAt: Date? = null
}