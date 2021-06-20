package kr.ac.kpu.lbs_platform.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.poko.remote.Place
import java.net.URLDecoder

open class AddDocumentAdapter(private val data: MutableList<Place>):
    RecyclerView.Adapter<AddDocumentAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val documentCountTextView: TextView = view.findViewById(R.id.documentCountTextView)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.add_document_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.documentCountTextView.text = String.format(viewHolder.documentCountTextView.text.toString(), URLDecoder.decode(data[position].fields.place_name, "UTF-8"))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.size

}
