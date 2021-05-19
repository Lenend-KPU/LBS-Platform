package kr.ac.kpu.lbs_platform.poko.remote

class LikeRequest(_success: Boolean = false, _status: Int = 0, _comment: String = "", val result: Array<Like> = arrayOf()): Request(_success, _status, _comment) {

}