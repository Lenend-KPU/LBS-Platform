package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.adapter.PlaceAdapter
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.global.Profile
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
            getPlacesFromServer()
        }
        // Inflate the layout for this fragment
        return inflated
    }

    fun getPlacesFromServer() {
        val profile = Profile.profile?.pk
        RequestHelper.Builder(PlaceRequest::class)
            .apply {
                this.currentFragment = this@PlaceFragment
                this.destFragment = null
                this.urlParameter = "profiles/$profile/places/"
                this.method = Request.Method.GET
                this.params = params
                this.onSuccessCallback = {
                    val placeRequest = it
                    placeRecyclerView.adapter = PlaceAdapter(placeRequest)
                }
            }
            .build()
            .request()
    }

}