package kr.ac.kpu.lbs_platform.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.fragment.AddPlaceFragment
import kr.ac.kpu.lbs_platform.poko.remote.Place
import kr.ac.kpu.lbs_platform.poko.remote.PlaceRequest
import java.net.URLDecoder

open class PlaceAdapter(private val dataSet: Array<Place>, private val fragment: Fragment? = null, private val activity: Activity? = null):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    init {
        Log.i("PlaceAdapter", dataSet.toString())
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeItemImageView: ImageView = view.findViewById(R.id.placeItemImageView)
        val nameTextView: TextView = view.findViewById(R.id.placeItemNameTextView)
        val ratingBar: RatingBar = view.findViewById(R.id.placeRatingBar)
    }

    open fun hideRatingBarAction(viewHolder: ViewHolder) {
        viewHolder.ratingBar.visibility = View.GONE
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
        hideRatingBarAction(viewHolder)

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        fragment?.let {
            Glide.with(it).load(dataSet[position].fields.place_photo).fitCenter().into(viewHolder.placeItemImageView)
        }
        activity?.let {
            Glide.with(it).load(dataSet[position].fields.place_photo).fitCenter().into(viewHolder.placeItemImageView)
        }

        viewHolder.nameTextView.text = URLDecoder.decode(dataSet[position].fields.place_name, "UTF-8")
        if(dataSet[position].fields.place_rate.isDigitsOnly()) {
            viewHolder.ratingBar.rating = dataSet[position].fields.place_rate.toFloat()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
