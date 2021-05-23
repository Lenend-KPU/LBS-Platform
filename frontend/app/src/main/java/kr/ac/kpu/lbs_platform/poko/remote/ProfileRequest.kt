package kr.ac.kpu.lbs_platform.poko.remote

class ProfileRequest(_success: Boolean = false, _status: Int = 0, _comment: String = "", val result: Profile? = null): Request(_success, _status, _comment) {
    override fun toString(): String {
        return "${super.toString()}, result: ${result.toString()}"
    }
}