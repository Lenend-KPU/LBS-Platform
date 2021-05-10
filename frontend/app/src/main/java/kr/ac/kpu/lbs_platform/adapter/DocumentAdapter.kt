package kr.ac.kpu.lbs_platform.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.poko.remote.DocumentRequest
import java.math.MathContext

class DocumentAdapter(private val dataSet: DocumentRequest):
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
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val documentItemName: TextView = view.findViewById(R.id.documentItemName)
        val documentPlaceRecyclerView: RecyclerView = view.findViewById(R.id.documentPlaceRecyclerView)
        init {
            context = view.context
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
