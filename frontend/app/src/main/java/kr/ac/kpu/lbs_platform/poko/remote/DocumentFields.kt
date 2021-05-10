package kr.ac.kpu.lbs_platform.poko.remote

data class DocumentFields (
    val profile: Int = 0,
    val profile_friend: Int? = 0,
    val document_name: String = "",
    val document_color: String = "",
    val document_map: String = "",
    val document_date: String = "",
    val document_liked: String = "",
    val document_private: Boolean = true,
)
