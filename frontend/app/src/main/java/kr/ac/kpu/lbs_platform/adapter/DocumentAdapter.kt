package kr.ac.kpu.lbs_platform.adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.activity.MainActivity
import kr.ac.kpu.lbs_platform.poko.remote.Document
import kr.ac.kpu.lbs_platform.poko.remote.DocumentRequest
import java.math.MathContext
import kotlin.properties.Delegates

class DocumentAdapter(private val dataSet: DocumentRequest, private val state: Bundle?):
    RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {
    companion object {
        lateinit var context: Context
    }

    init {
        Log.i("PlaceAdapter", dataSet.toString())
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {
        lateinit var document: Document
        val documentItemName: TextView = view.findViewById(R.id.documentItemName)
        val documentPlaceRecyclerView: RecyclerView = view.findViewById(R.id.documentPlaceRecyclerView)
        val mapView: MapView = view.findViewById(R.id.mapView)
        init {
            context = view.context
        }

        override fun onMapReady(googleMap: GoogleMap?) {
            var idx = 0
            var latSum = 0.0
            var longSum = 0.0
            for(place in document.places) {
                idx++
                val latLng = LatLng(place.fields.place_latitude.toDouble(), place.fields.place_longitude.toDouble())
                val marker = googleMap?.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("place $idx")
                )
                latSum += latLng.latitude
                longSum += latLng.longitude
            }
            if(idx != 0) {
                val latAvg = latSum / idx
                val longAvg = latSum / idx
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latAvg, longAvg), 10f))
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
        viewHolder.documentPlaceRecyclerView.layoutManager = LinearLayoutManager(context)
        viewHolder.documentPlaceRecyclerView.adapter = PlaceAdapter(dataSet.result[position].places)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.result.size


}
