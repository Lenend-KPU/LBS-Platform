package kr.ac.kpu.lbs_platform.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.poko.remote.Place
import kr.ac.kpu.lbs_platform.poko.remote.PlaceRequest

open class PlaceAdapter(private val dataSet: Array<Place>):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    init {
        Log.i("PlaceAdapter", dataSet.toString())
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoTextView: TextView = view.findViewById(R.id.placeItemPhotoTextView)
        val nameTextView: TextView = view.findViewById(R.id.placeItemNameTextView)
        val rateTextView: TextView = view.findViewById(R.id.placeRateTextView)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.place_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.photoTextView.text = dataSet[position].fields.place_photo
        viewHolder.nameTextView.text = dataSet[position].fields.place_name
        viewHolder.rateTextView.text = dataSet[position].fields.place_rate
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
