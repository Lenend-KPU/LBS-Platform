package kr.ac.kpu.lbs_platform.poko.remote

data class PlaceFields(
    val profile: Int = 0,
    val place_name: String = "",
    val place_rate: String = "",
    val place_photo: String = "",
    val place_latitude: String = "",
    val place_longitude: String = "",
    val place_private: Boolean = true,
)
