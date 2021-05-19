package kr.ac.kpu.lbs_platform.poko.remote

data class Save(
    val model: String = "", val pk: Int = 0, val fields: SaveFields = SaveFields()
)