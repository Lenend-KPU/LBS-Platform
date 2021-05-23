package kr.ac.kpu.lbs_platform.poko.remote

data class FollowingFields (
    val friend_profile: Profile,
    val friend_date: String,
    val friend_status: Int
)
