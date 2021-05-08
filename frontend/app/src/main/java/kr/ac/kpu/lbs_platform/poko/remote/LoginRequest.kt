package kr.ac.kpu.lbs_platform.poko.remote

class LoginRequest(_success: Boolean = false, _status: Int = 0, _comment: String = "", val userid: Int = 0): Request(_success, _status, _comment) {
    override fun toString(): String {
        return "${super.toString()}, userid: $userid"
    }
}