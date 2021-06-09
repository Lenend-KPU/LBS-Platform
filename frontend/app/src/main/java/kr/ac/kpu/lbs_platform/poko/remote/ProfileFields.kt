package kr.ac.kpu.lbs_platform.poko.remote

data class ProfileFields(var user: Int = 0, var profile_name: String = "", var profile_photo: String = "", var profile_follower: Int = 0, var profile_following: Int = 0, var profile_private: Boolean = true)
