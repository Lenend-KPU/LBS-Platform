package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import kr.ac.kpu.lbs_platform.activity.MainActivity
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.global.FragmentChanger

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
        val toolbar = inflated.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener onOptionsItemSelected(it)
        }
        return inflated
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        MainActivity.previousItemId = item.itemId
        when(item.itemId) {
            R.id.generate_place -> {
                FragmentChanger.change(this, AddPlaceFragment())
            }
            R.id.generate_route -> {
                FragmentChanger.change(this, AddDocumentFragment())
            }
            R.id.saved -> {
                FragmentChanger.change(this, SavedLocationFragment())
            }
            R.id.profile_edit -> {
                FragmentChanger.change(this, ProfileEditFragment())
            }
        }
        return true
    }
}