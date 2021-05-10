package kr.ac.kpu.lbs_platform.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.poko.remote.Place
import kr.ac.kpu.lbs_platform.poko.remote.PlaceRequest


class SelectPlaceAdapter(val _dataSet: Array<Place>, private val onClick: (View) -> Unit):
    PlaceAdapter(_dataSet) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.place_item, viewGroup, false)

        view.setOnClickListener {
            onClick(it)
        }

        return ViewHolder(view)
    }
}
