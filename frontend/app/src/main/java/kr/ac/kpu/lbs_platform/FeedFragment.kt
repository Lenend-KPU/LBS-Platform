package kr.ac.kpu.lbs_platform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class FeedFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val inflated = inflater.inflate(R.layout.fragment_feed, container, false)
        MainActivity.instance?.binding?.bottomNavigationView?.visibility = View.VISIBLE
        MainActivity.instance?.binding?.bottomNavigationView?.selectedItemId = R.id.page_feed
        // Inflate the layout for this fragment
        return inflated
    }
}