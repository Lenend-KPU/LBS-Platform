package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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

class SavedLocationFragment : Fragment(), Invalidatable {
    lateinit var feedRecyclerView: RecyclerView
    var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bundle = savedInstanceState
        val inflated = inflater.inflate(R.layout.fragment_feed, container, false)
        MainActivity.instance?.binding?.bottomNavigationView?.visibility = View.VISIBLE
        MainActivity.instance?.binding?.bottomNavigationView?.selectedItemId = R.id.page_feed
        // Inflate the layout for this fragment
        val toolbar = inflated.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener onOptionsItemSelected(it)
        }
        feedRecyclerView = inflated.findViewById<RecyclerView>(R.id.feedRecyclerView)
        feedRecyclerView.layoutManager = LinearLayoutManager(this.activity)
        getSavesFromServer(feedRecyclerView, savedInstanceState)
        return inflated
    }

    fun getSavesFromServer(recyclerView: RecyclerView, savedInstanceState: Bundle?) {
        Log.i(this::class.java.name, "getSavesFromServer")
        val profilePk = Profile.profile!!.pk
        RequestHelper.Builder(DocumentRequest::class)
            .apply {
                this.currentFragment = this@SavedLocationFragment
                this.destFragment = null
                this.urlParameter = "profiles/$profilePk/save/"
                this.method = com.android.volley.Request.Method.GET
                this.onSuccessCallback = {
                    recyclerView.adapter = DocumentAdapter(it, savedInstanceState, MainActivity.instance!!, this@SavedLocationFragment)
                }
            }
            .build()
            .request()
    }

    override fun invalidateRecyclerView() {
        getSavesFromServer(feedRecyclerView, bundle)
    }
}