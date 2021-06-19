package kr.ac.kpu.lbs_platform.global

import androidx.fragment.app.Fragment
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.fragment.ProfileFragment

object FragmentChanger {
    fun change(from: Fragment, to: Fragment, view: Int = R.id.mainActivityfragment) {
        from.activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(view, to)
            ?.commit()
    }
}