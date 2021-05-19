package kr.ac.kpu.lbs_platform.poko.remote

data class Like(
    val model: String = "", val pk: Int = 0, val fields: CommentFields = CommentFields()
)