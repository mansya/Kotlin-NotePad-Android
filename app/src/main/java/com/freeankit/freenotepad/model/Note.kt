package com.freeankit.freenotepad.model

import com.google.firebase.database.Exclude
import java.io.Serializable


/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */


data class Note(var id: String = "",
                var text: String? = null,
                var title: String? = null,
                var isPinned: Boolean = false,
                var simple_rate: Int? = null,
                var achieve_rate: Int? = null,
                var dev_difficulty_rate: Int? = null,
                var everyone_rate: Int? = null,
                var everyday_rate: Int? = null,
                var overall_rate: Int? = null,
                var color: Int? = null,
                var uid: String? = null) : Serializable {

    @Exclude
    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["id"] = id
        text?.let { result.put("text", it) }
        title?.let { result.put("title", it) }
        simple_rate?.let { result.put("simple_rate", it) }
        achieve_rate?.let { result.put("achieve_rate", it) }
        dev_difficulty_rate?.let { result.put("dev_difficulty_rate", it) }
        everyone_rate?.let { result.put("everyone_rate", it) }
        everyday_rate?.let { result.put("everyday_rate", it) }
        overall_rate?.let { result.put("overall_rate", it) }
        color?.let { result.put("color", it) }
        result["isPinned"] = isPinned
        uid?.let { result.put("uid", it) }
        return result
    }
}
