package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.activity.MainActivity
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.adapter.DocumentAdapter
import kr.ac.kpu.lbs_platform.global.FragmentChanger
import kr.ac.kpu.lbs_platform.global.Profile
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.poko.remote.DocumentRequest

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
        val feedRecyclerView = inflated.findViewById<RecyclerView>(R.id.feedRecyclerView)
        feedRecyclerView.layoutManager = LinearLayoutManager(this.activity)
        getFeedsFromServer(feedRecyclerView, savedInstanceState)
        return inflated
    }

    fun getFeedsFromServer(recyclerView: RecyclerView, savedInstanceState: Bundle?) {
        Log.i(this::class.java.name, "getFeedsFromServer")
        RequestHelper.Builder(DocumentRequest::class)
            .apply {
                this.currentFragment = this@FeedFragment
                this.destFragment = null
                this.urlParameter = "feed/"
                this.method = com.android.volley.Request.Method.GET
                this.onSuccessCallback = {
                    recyclerView.adapter = DocumentAdapter(it, savedInstanceState)
                }
            }
            .build()
            .request()
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
                FragmentChanger.change(this, ProfileFragment())
            }
            R.id.profile_edit -> {
                FragmentChanger.change(this, ProfileEditFragment())
            }
        }
        return true
    }
}