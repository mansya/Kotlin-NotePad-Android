package com.freeankit.freenotepad.model

import com.google.firebase.database.Exclude


/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
data class Note(var id: Int = -1,
                var text: String? = null,
                var isPinned: Boolean = false) {
    @Exclude
    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["id"] = id
        text?.let { result.put("text", it) }
        result["isPinned"] = isPinned
        return result
    }

}
