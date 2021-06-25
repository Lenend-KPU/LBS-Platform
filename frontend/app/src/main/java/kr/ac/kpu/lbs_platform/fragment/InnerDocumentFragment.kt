package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.adapter.CommentAdapter
import kr.ac.kpu.lbs_platform.adapter.PlaceAdapter
import kr.ac.kpu.lbs_platform.global.FragmentChanger
import kr.ac.kpu.lbs_platform.global.Profile
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.poko.remote.*


class InnerDocumentFragment(var document: Document) : Fragment(), Invalidatable, OnMapReadyCallback {
    lateinit var inflated: View

    lateinit var documentProfileName: TextView
    lateinit var documentProfileButton: TextView
    lateinit var documentItemName: TextView
    lateinit var documentPlaceRecyclerView: RecyclerView
    lateinit var commentRecyclerView: RecyclerView
    lateinit var documentLikeCountTextView: TextView
    lateinit var documentLikeButton: TextView
    lateinit var documentSaveCountTextView: TextView
    lateinit var documentSaveButton: TextView
    lateinit var commentSubmitButton: TextView
    lateinit var commentEditText: TextView
    lateinit var documentDeleteButton: Button
    lateinit var mapView: SupportMapFragment
    lateinit var map: GoogleMap
    var state: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = savedInstanceState
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        bindData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.inner_document_item, container, false)
        documentProfileName = view.findViewById(R.id.documentProfileName)
        documentProfileButton = view.findViewById(R.id.documentProfileButton)
        documentItemName = view.findViewById(R.id.documentItemName)
        documentPlaceRecyclerView = view.findViewById(R.id.documentPlaceRecyclerView)
        commentRecyclerView = view.findViewById(R.id.documentCommentRecyclerView)
        documentLikeCountTextView = view.findViewById(R.id.documentLikeCountTextView)
        documentLikeButton = view.findViewById(R.id.documentLikeButton)
        documentSaveCountTextView = view.findViewById(R.id.documentSaveCountTextView)
        documentSaveButton = view.findViewById(R.id.documentSaveButton)
        commentSubmitButton = view.findViewById(R.id.commentSubmitButton)
        commentEditText = view.findViewById(R.id.commentEditText)
        documentDeleteButton = view.findViewById(R.id.documentDeleteButton)

        return view
    }

    fun bindData() {
        mapView.onCreate(state)
        mapView.onResume()
        mapView.getMapAsync(this)
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        documentItemName.text = document.fields.document_name
        getProfileFromServer(document.fields.profile) {
            val profile = it.result!!
            documentProfileName.text = profile.fields.profile_name
            documentProfileButton.setOnClickListener {
                FragmentChanger.change(this as Fragment, ProfileFragment(profile))
            }
        }

        documentDeleteButton.let {
            if (Profile.profile!!.pk == document.fields.profile) {
                it.visibility = View.VISIBLE
                it.setOnClickListener {
                    deleteDocumentToServer()
                }
            } else {
                it.visibility = View.GONE
            }
        }


        documentPlaceRecyclerView.layoutManager = LinearLayoutManager(activity)
        documentPlaceRecyclerView.adapter = PlaceAdapter(document.places, this as Fragment)
        commentRecyclerView.layoutManager = LinearLayoutManager(activity)
        commentRecyclerView.adapter = CommentAdapter(document.comments, document.fields.profile, this as Invalidatable)
        documentLikeCountTextView.text = document.likes.size.toString()
        documentLikeButton.setOnClickListener {
            sendLikeToServer()
        }
        documentSaveCountTextView.text = document.saves.size.toString()
        documentSaveButton.setOnClickListener {
            sendSaveToServer()
        }
        commentSubmitButton.setOnClickListener {
            val comment = commentEditText.text.toString()
            sendCommentToServer(comment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        var idx = 0
        val latLngs = mutableListOf<LatLng>()
        for(place in document.places) {
            idx++
            val latLng = LatLng(place.fields.place_latitude.toDouble(), place.fields.place_longitude.toDouble())
            latLngs.add(latLng)
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("place $idx")
            )
        }
        val maxLat = latLngs.reduce { acc, latLng -> if(acc.latitude < latLng.latitude ) latLng else acc }
        val minLat = latLngs.reduce { acc, latLng -> if(acc.latitude > latLng.latitude ) latLng else acc }
        val maxLong = latLngs.reduce { acc, latLng -> if(acc.latitude > latLng.longitude ) latLng else acc }
        val minLong = latLngs.reduce { acc, latLng -> if(acc.latitude < latLng.longitude ) latLng else acc }

        val bounds = LatLngBounds(LatLng(minLat.latitude, maxLong.longitude), LatLng(maxLat.latitude, minLong.longitude))

        requireActivity().runOnUiThread {
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40))
        }
    }

    fun getProfileFromServer(profileId: Int, callback: (ProfileRequest) -> Unit) {
        Log.i(this::class.java.name, "getProfileFromServer")
        RequestHelper.Builder(ProfileRequest::class)
            .apply {
                this.destFragment = null
                this.urlParameter = "profiles/$profileId/"
                this.method = com.android.volley.Request.Method.GET
                this.onSuccessCallback = callback
            }
            .build()
            .request()
    }

    fun sendCommentToServer(comment: String) {
        Log.i(this::class.java.name, "sendCommentToServer")
        val profileNumber = document.fields.profile
        val myProfileNumber = Profile.profile?.pk ?: 0
        val documentNumber = document.pk
        val params = mutableMapOf<String, String>()
        params["me"] = myProfileNumber.toString()
        params["text"] = comment
        RequestHelper.Builder(DocumentRequest::class)
            .apply {
                this.destFragment = null
                this.urlParameter = "profiles/$profileNumber/documents/$documentNumber/comments/"
                this.method = com.android.volley.Request.Method.POST
                this.onSuccessCallback = {
                    commentEditText.text = ""
                    invalidateRecyclerView()
                }
                this.params = params
            }
            .build()
            .request()
    }

    fun sendLikeToServer() {
        Log.i(this::class.java.name, "sendLikeToServer")
        val profileNumber = document.fields.profile
        val myProfileNumber = Profile.profile?.pk ?: 0
        val documentNumber = document.pk
        val params = mutableMapOf<String, String>()
        params["me"] = myProfileNumber.toString()
        RequestHelper.Builder(Request::class)
            .apply {
                this.destFragment = null
                this.urlParameter = "profiles/$profileNumber/documents/$documentNumber/likes/"
                this.method = com.android.volley.Request.Method.POST
                this.onSuccessCallback = {
                    invalidateRecyclerView()
                }
                this.params = params
            }
            .build()
            .request()
    }

    fun sendSaveToServer() {
        Log.i(this::class.java.name, "sendSaveToServer")
        val profileNumber = document.fields.profile
        val myProfileNumber = Profile.profile?.pk ?: 0
        val documentNumber = document.pk
        val params = mutableMapOf<String, String>()
        params["me"] = myProfileNumber.toString()
        RequestHelper.Builder(Request::class)
            .apply {
                this.destFragment = null
                this.urlParameter = "profiles/$profileNumber/documents/$documentNumber/saves/"
                this.method = com.android.volley.Request.Method.POST
                this.onSuccessCallback = {
                    invalidateRecyclerView()
                }
                this.params = params
            }
            .build()
            .request()
    }

    fun deleteDocumentToServer() {
        Log.i(this::class.java.name, "sendSaveToServer")
        val profileNumber = document.fields.profile
        val documentNumber = document.pk
        RequestHelper.Builder(Request::class)
            .apply {
                this.destFragment = null
                this.urlParameter = "profiles/$profileNumber/documents/$documentNumber/"
                this.method = com.android.volley.Request.Method.DELETE
                this.onSuccessCallback = {
                    invalidateRecyclerView()
                }
            }
            .build()
            .request()
    }

    override fun invalidateRecyclerView() {
        Log.i(this::class.java.name, "sendSaveToServer")
        val profileNumber = document.fields.profile
        val documentNumber = document.pk
        RequestHelper.Builder(DocumentRequest::class)
        .apply {
            this.destFragment = null
            this.urlParameter = "profiles/$profileNumber/documents/$documentNumber/"
            this.method = com.android.volley.Request.Method.GET
            this.onSuccessCallback = {
                document = it.result[0]
                bindData()
            }
        }
        .build()
        .request()
    }
}