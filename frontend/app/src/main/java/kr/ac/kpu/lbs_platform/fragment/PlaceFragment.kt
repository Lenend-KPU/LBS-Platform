package kr.ac.kpu.lbs_platform.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.adapter.PlaceAdapter
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.global.User
import kr.ac.kpu.lbs_platform.poko.remote.PlaceRequest

class PlaceFragment : Fragment() {
    lateinit var placeRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val inflated = inflater.inflate(R.layout.fragment_place, container, false)

        val layoutManager = LinearLayoutManager(activity)

        placeRecyclerView = inflated.findViewById(R.id.placeRecyclerView)
        placeRecyclerView.layoutManager = layoutManager

        GlobalScope.launch {
            getPlacesFromServer(this@PlaceFragment) {
                placeRecyclerView.adapter = PlaceAdapter(it)
            }
        }
        // Inflate the layout for this fragment
        return inflated
    }

    companion object {
        fun getPlacesFromServer(currentFragment: Fragment? = null,
                                currentActivity: Activity? = null,
                                callback: (PlaceRequest) -> Unit = {})
        {
            val userid = User.userid
            RequestHelper.Builder(PlaceRequest::class)
                .apply {
                    this.currentFragment = currentFragment
                    this.destFragment = null
                    this.activity = currentActivity
                    this.urlParameter = "profiles/$userid/places/"
                    this.method = Request.Method.GET
                    this.onSuccessCallback = callback
                }
                .build()
                .request()
        }
    }



}