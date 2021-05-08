package kr.ac.kpu.lbs_platform.poko.remote

import com.google.android.libraries.places.api.model.Place

open class Request(open val success: Boolean = false, open val status: Int = 0, open val comment: String = "") {
    override fun toString(): String {
        return "success: $success, status: $status, comment: $comment"
    }
}