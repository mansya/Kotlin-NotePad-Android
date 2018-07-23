package com.freeankit.freenotepad.model

import com.google.firebase.database.Exclude
import java.io.Serializable


/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */


data class Note(var id: String = "",
                var text: String? = null,
                var title: String? = null,
                var isPinned: Boolean = false) : Serializable {


    @Exclude
    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["id"] = id
        text?.let { result.put("text", it) }
        title?.let { result.put("title", it) }
        result["isPinned"] = isPinned
        return result
    }

}
