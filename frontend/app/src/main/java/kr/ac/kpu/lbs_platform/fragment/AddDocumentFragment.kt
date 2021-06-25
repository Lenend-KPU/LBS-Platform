package kr.ac.kpu.lbs_platform.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.activity.MainActivity
import kr.ac.kpu.lbs_platform.activity.MainActivity.Companion.instance
import kr.ac.kpu.lbs_platform.activity.SelectPlaceActivity
import kr.ac.kpu.lbs_platform.adapter.AddDocumentAdapter
import kr.ac.kpu.lbs_platform.global.Profile
import kr.ac.kpu.lbs_platform.global.RequestCode
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.poko.remote.Place
import kr.ac.kpu.lbs_platform.poko.remote.Request
import splitties.toast.toast

class AddDocumentFragment : Fragment(), Invalidatable {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        var places = mutableListOf<Place>()
        lateinit var instance: AddDocumentFragment
    }

    lateinit var inflated: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        MainActivity.instance!!.invalidateBottomNavigationRoute()

        instance = this
        places = mutableListOf<Place>()
        inflated = inflater.inflate(R.layout.fragment_add_document, container, false)

        val addPlaceButton = inflated.findViewById<Button>(R.id.addPlaceButton)

        addPlaceButton.setOnClickListener {
            if(places.size >= 10) {
                toast("places 10개 초과")
            }
            val intent = Intent(this.activity, SelectPlaceActivity::class.java)
            this.activity?.startActivityForResult(intent, RequestCode.SELECT_PLACE_REQUEST_CODE)
        }

        val submitDocumentButton = inflated.findViewById<Button>(R.id.submitDocumentButton)
        val documentNameEditText = inflated.findViewById<EditText>(R.id.documentNameEditText)
        submitDocumentButton.setOnClickListener {
            val title = documentNameEditText.text.toString()
            if(title == "" || places.isEmpty()) {
                toast("비어있는 필드가 있습니다.")
                return@setOnClickListener
            }
            postDocumentToServer(title)
        }
        val addDocumentRecyclerView = inflated.findViewById<RecyclerView>(R.id.addDocumentRecyclerView)
        addDocumentRecyclerView.layoutManager = LinearLayoutManager(this.activity)
        addDocumentRecyclerView.adapter = AddDocumentAdapter(places, this)
        // Inflate the layout for this fragment
        return inflated
    }

    fun postDocumentToServer(title: String) {
        val params = mutableMapOf<String, String>()
        params["name"] = title
        params["color"] = "blue"
        params["private"] = "true"
        var cnt = 0
        for(place in places) {
            cnt += 1
            params["place$cnt"] = place.pk.toString()
        }
        val profileNumber = Profile.profile?.pk.toString()
        RequestHelper.Builder(Request::class)
            .apply {
                this.currentFragment = this@AddDocumentFragment
                this.activity = MainActivity.instance
                this.destFragment = FeedFragment()
                this.urlParameter = "profiles/$profileNumber/documents/"
                this.params = params
            }
            .build()
            .request()
    }

    fun notifyDataHasChanged() {
        val addDocumentRecyclerView = inflated.findViewById<RecyclerView>(R.id.addDocumentRecyclerView)
        addDocumentRecyclerView.adapter = AddDocumentAdapter(places, this)
    }

    override fun invalidateRecyclerView() {
        notifyDataHasChanged()
    }
}