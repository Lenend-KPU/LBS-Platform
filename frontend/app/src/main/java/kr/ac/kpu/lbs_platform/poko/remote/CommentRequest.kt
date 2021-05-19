package kr.ac.kpu.lbs_platform.poko.remote

class CommentRequest(_success: Boolean = false, _status: Int = 0, _comment: String = "", val result: Array<Comment> = arrayOf()): Request(_success, _status, _comment)