package kr.ac.kpu.lbs_platform.poko.remote

data class CommentFields(
    val profile: Int = 0,
    val document: Int? = 0,
    val comment_text: String = "",
    val comment_date: String = "",
)