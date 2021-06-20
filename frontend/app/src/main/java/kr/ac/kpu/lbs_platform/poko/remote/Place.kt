package kr.ac.kpu.lbs_platform.poko.remote

import java.io.Serializable

data class Place(val model: String = "", val pk: Int = 0, val fields: PlaceFields = PlaceFields()) : Serializable