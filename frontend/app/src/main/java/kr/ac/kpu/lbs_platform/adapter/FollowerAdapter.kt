package kr.ac.kpu.lbs_platform.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.fragment.Invalidatable
import kr.ac.kpu.lbs_platform.poko.remote.FriendRequest

class FollowerAdapter(private val request: FriendRequest, fragment: Invalidatable):
    RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {

    init {
        Log.i("FollowerAdapter", request.toString())
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val followerIdTextView: TextView = view.findViewById(R.id.followerIdTextView)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.follower_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.followerIdTextView.text = request.result.followers[position].fields.profile.fields.profile_name
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = request.result.followers.size

}
