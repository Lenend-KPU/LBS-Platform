package kr.ac.kpu.lbs_platform.poko.remote

class FriendRequest(_success: Boolean = false, _status: Int = 0, _comment: String = "", val result: FriendFields): Request(_success, _status, _comment) {
    override fun toString(): String {
        return "${super.toString()}, $result"
    }
}