package kr.ac.kpu.lbs_platform.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.fragment.Invalidatable
import kr.ac.kpu.lbs_platform.poko.remote.FriendRequest

class FollowingAdapter(private val request: FriendRequest, val fragment: Fragment):
    RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    init {
        Log.i("FollowingAdapter", request.toString())
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val followerIdTextView: TextView = view.findViewById(R.id.followerIdTextView)
        val followerImageView: ImageView = view.findViewById(R.id.followerImageView)
        val unfollowImageView: TextView = view.findViewById(R.id.unfollowImageView)
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.follower_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.followerIdTextView.text = request.result.followings[position].fields.friend_profile.fields.profile_name
        Glide.with(fragment).load(request.result.followings[position].fields.friend_profile.fields.profile_photo).into(viewHolder.followerImageView)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = request.result.followers.size

}
