package kr.ac.kpu.lbs_platform.poko.remote

import java.io.Serializable

class PlaceRequest(_success: Boolean = false, _status: Int = 0, _comment: String = "", val result: Array<Place>? = null): Request(_success, _status, _comment) {
    override fun toString(): String {
        return "${super.toString()}, result: ${result.contentToString()}"
    }
}