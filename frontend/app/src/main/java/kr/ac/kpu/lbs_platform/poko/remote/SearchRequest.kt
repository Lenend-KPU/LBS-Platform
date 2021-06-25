package kr.ac.kpu.lbs_platform.poko.remote

data class SearchRequest(val hits: Hits): Request(true, 200, "")