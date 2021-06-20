package kr.ac.kpu.lbs_platform.adapter

import android.app.Activity
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
import kr.ac.kpu.lbs_platform.fragment.AddProfileFragment
import kr.ac.kpu.lbs_platform.fragment.Invalidatable
import kr.ac.kpu.lbs_platform.fragment.ProfileFragment
import kr.ac.kpu.lbs_platform.global.FragmentChanger
import kr.ac.kpu.lbs_platform.global.Profile
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.poko.remote.Document
import kr.ac.kpu.lbs_platform.poko.remote.DocumentRequest
import kr.ac.kpu.lbs_platform.poko.remote.ProfileRequest
import kr.ac.kpu.lbs_platform.poko.remote.Request

class DocumentAdapter(private val dataSet: DocumentRequest, private val state: Bundle?, private val activity: Activity, private val fragment: Invalidatable):
    RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {
    companion object {
        lateinit var activity: Activity
    }

    init {
        Log.i("PlaceAdapter", dataSet.toString())
        DocumentAdapter.activity = activity
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {
        lateinit var document: Document
        val documentProfileName: TextView = view.findViewById(R.id.documentProfileName)
        val documentProfileButton: TextView = view.findViewById(R.id.documentProfileButton)
        val documentItemName: TextView = view.findViewById(R.id.documentItemName)
        val documentPlaceRecyclerView: RecyclerView = view.findViewById(R.id.documentPlaceRecyclerView)
        val commentRecyclerView: RecyclerView = view.findViewById(R.id.commentRecyclerView)
        val documentLikeCountTextView: TextView = view.findViewById(R.id.documentLikeCountTextView)
        val documentLikeButton: TextView = view.findViewById(R.id.documentLikeButton)
        val documentSaveCountTextView: TextView = view.findViewById(R.id.documentSaveCountTextView)
        val documentSaveButton: TextView = view.findViewById(R.id.documentSaveButton)
        val commentSubmitButton: TextView = view.findViewById(R.id.commentSubmitButton)
        val commentEditText: TextView = view.findViewById(R.id.commentEditText)
        val documentDeleteButton: Button = view.findViewById(R.id.documentDeleteButton)

        val mapView: MapView = view.findViewById(R.id.mapView)

        override fun onMapReady(googleMap: GoogleMap?) {
            var idx = 0
            var latSum = 0.0
            var longSum = 0.0
            val latLngs = mutableListOf<LatLng>()
            for(place in document.places) {
                idx++
                val latLng = LatLng(place.fields.place_latitude.toDouble(), place.fields.place_longitude.toDouble())
                latLngs.add(latLng)
                googleMap?.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("place $idx")
                )
                latSum += latLng.latitude
                longSum += latLng.longitude
            }
            var bounds: LatLngBounds? = null
            for(index in 0 until latLngs.size - 1) {
                val first = latLngs[index]
                val second = latLngs[index+1]
                val newBounds = if(second.latitude > first.latitude) LatLngBounds(first, second) else LatLngBounds(second, first)
                if(bounds == null) {
                    bounds = newBounds
                } else {
                    bounds.including(latLngs[index + 1])
                }
            }
            bounds?.let {
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            } ?: run {
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs[0], 10f))
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.document_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.document = dataSet.result[position]
        viewHolder.mapView.onCreate(state)
        viewHolder.mapView.getMapAsync(viewHolder)
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.documentItemName.text = dataSet.result.let {
            return@let it[position].fields.document_name
        }
        dataSet.result.let {
            getProfileFromServer(viewHolder, position, it[position].fields.profile) {
                val profile = it.result!!
                viewHolder.documentProfileName.text = profile.fields.profile_name
                viewHolder.documentProfileButton.setOnClickListener {
                    Profile.selectedProfile = profile
                    FragmentChanger.change(fragment as Fragment, ProfileFragment())
                }
            }
        }

        viewHolder.documentDeleteButton.let {
            if (Profile.profile!!.pk == dataSet.result[position].fields.profile) {
                it.visibility = View.VISIBLE
                it.setOnClickListener {
                    deleteDocumentToServer(position)
                }
            } else {
                it.visibility = View.GONE
            }
        }



        viewHolder.documentPlaceRecyclerView.layoutManager = LinearLayoutManager(activity)
        viewHolder.documentPlaceRecyclerView.adapter = PlaceAdapter(dataSet.result[position].places, fragment as Fragment)
        viewHolder.commentRecyclerView.layoutManager = LinearLayoutManager(activity)
        viewHolder.commentRecyclerView.adapter = CommentAdapter(dataSet.result[position].comments, dataSet.result[position].fields.profile, fragment)
        viewHolder.documentLikeCountTextView.text = dataSet.result[position].likes.size.toString()
        viewHolder.documentLikeButton.setOnClickListener {
            sendLikeToServer(position)
        }
        viewHolder.documentSaveCountTextView.text = dataSet.result[position].saves.size.toString()
        viewHolder.documentSaveButton.setOnClickListener {
            sendSaveToServer(position)
        }
        viewHolder.commentSubmitButton.setOnClickListener {
            val comment = viewHolder.commentEditText.text
            sendCommentToServer(viewHolder, position, comment.toString())
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.result.size

    fun getProfileFromServer(viewHolder: ViewHolder, position: Int, profileId: Int, callback: (ProfileRequest) -> Unit) {
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

    fun sendCommentToServer(viewHolder: ViewHolder, position: Int, comment: String) {
        Log.i(this::class.java.name, "sendCommentToServer")
        val profileNumber = dataSet.result[position].fields.profile
        val myProfileNumber = Profile.profile?.pk ?: 0
        val documentNumber = dataSet.result[position].pk
        val params = mutableMapOf<String, String>()
        params["me"] = myProfileNumber.toString()
        params["text"] = comment
        RequestHelper.Builder(DocumentRequest::class)
            .apply {
                this.activity = activity
                this.destFragment = null
                this.urlParameter = "profiles/$profileNumber/documents/$documentNumber/comments/"
                this.method = com.android.volley.Request.Method.POST
                this.onSuccessCallback = {
                    viewHolder.commentEditText.text = ""
                    fragment.invalidateRecyclerView()
                }
                this.params = params
            }
            .build()
            .request()
    }

    fun sendLikeToServer(position: Int) {
        Log.i(this::class.java.name, "sendLikeToServer")
        val profileNumber = dataSet.result[position].fields.profile
        val myProfileNumber = Profile.profile?.pk ?: 0
        val documentNumber = dataSet.result[position].pk
        val params = mutableMapOf<String, String>()
        params["me"] = myProfileNumber.toString()
        RequestHelper.Builder(Request::class)
            .apply {
                this.activity = activity
                this.destFragment = null
                this.urlParameter = "profiles/$profileNumber/documents/$documentNumber/likes/"
                this.method = com.android.volley.Request.Method.POST
                this.onSuccessCallback = {
                    fragment.invalidateRecyclerView()
                }
                this.params = params
            }
            .build()
            .request()
    }

    fun sendSaveToServer(position: Int) {
        Log.i(this::class.java.name, "sendSaveToServer")
        val profileNumber = dataSet.result[position].fields.profile
        val myProfileNumber = Profile.profile?.pk ?: 0
        val documentNumber = dataSet.result[position].pk
        val params = mutableMapOf<String, String>()
        params["me"] = myProfileNumber.toString()
        RequestHelper.Builder(Request::class)
            .apply {
                this.activity = activity
                this.destFragment = null
                this.urlParameter = "profiles/$profileNumber/documents/$documentNumber/saves/"
                this.method = com.android.volley.Request.Method.POST
                this.onSuccessCallback = {
                    fragment.invalidateRecyclerView()
                }
                this.params = params
            }
            .build()
            .request()
    }

    fun deleteDocumentToServer(position: Int) {
        Log.i(this::class.java.name, "sendSaveToServer")
        val profileNumber = dataSet.result[position].fields.profile
        val documentNumber = dataSet.result[position].pk
        RequestHelper.Builder(Request::class)
            .apply {
                this.destFragment = null
                this.urlParameter = "profiles/$profileNumber/documents/$documentNumber/"
                this.method = com.android.volley.Request.Method.DELETE
                this.onSuccessCallback = {
                    fragment.invalidateRecyclerView()
                }
            }
            .build()
            .request()
    }

}
