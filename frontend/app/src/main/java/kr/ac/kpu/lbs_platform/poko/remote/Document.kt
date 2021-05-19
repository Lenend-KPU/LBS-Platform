package kr.ac.kpu.lbs_platform.poko.remote

data class Document(
    val model: String = "", val pk: Int = 0, val fields: DocumentFields = DocumentFields(),
    val comments: Array<Comment> = arrayOf(),
    val likes: Array<Any> = arrayOf(),
    val places: Array<Place> = arrayOf()
)