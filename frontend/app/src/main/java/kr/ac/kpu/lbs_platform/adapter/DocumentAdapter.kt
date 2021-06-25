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
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.fragment.InnerDocumentFragment
import kr.ac.kpu.lbs_platform.fragment.Invalidatable
import kr.ac.kpu.lbs_platform.global.FragmentChanger
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.poko.remote.Document
import kr.ac.kpu.lbs_platform.poko.remote.DocumentRequest
import kr.ac.kpu.lbs_platform.poko.remote.ProfileRequest
import java.net.URLDecoder

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
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var document: Document
        val documentItemName: TextView = view.findViewById(R.id.outerDocumentItemName)
        val outerDocumentCommentRecyclerView: RecyclerView = view.findViewById(R.id.outerDocumentCommentRecyclerView)
        val outerDocumentPlaceRecyclerView: RecyclerView = view.findViewById(R.id.outerDocumentPlaceRecyclerView)
        val outerDocumentProfileName: TextView = view.findViewById(R.id.outerDocumentProfileName)
        val outerDocumentLikeCountTextView: TextView = view.findViewById(R.id.outerDocumentLikeCountTextView)
        val outerDocumentSaveCountTextView: TextView = view.findViewById(R.id.outerDocumentSaveCountTextView)
        val outerDocumentGoInnerButton: Button = view.findViewById(R.id.outerDocumentGoInnerButton)
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
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.documentItemName.text = dataSet.result.let {
            return@let URLDecoder.decode(it[position].fields.document_name, "UTF-8")
        }

        viewHolder.outerDocumentPlaceRecyclerView.layoutManager = LinearLayoutManager(activity)
        viewHolder.outerDocumentPlaceRecyclerView.adapter = PlaceAdapter(viewHolder.document.places, fragment as Fragment)

        viewHolder.outerDocumentCommentRecyclerView.layoutManager = LinearLayoutManager(activity)
        viewHolder.outerDocumentCommentRecyclerView.adapter = CommentAdapter(viewHolder.document.comments, viewHolder.document.fields.profile, fragment, false)

        getProfileFromServer(viewHolder.document.fields.profile) {
            viewHolder.outerDocumentProfileName.text = it.result!!.fields.profile_name
        }
        viewHolder.outerDocumentLikeCountTextView.text = viewHolder.document.likes.size.toString()
        viewHolder.outerDocumentSaveCountTextView.text = viewHolder.document.saves.size.toString()

        viewHolder.outerDocumentGoInnerButton.setOnClickListener {
            FragmentChanger.change(fragment as Fragment, InnerDocumentFragment(dataSet.result[position]))
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.result.size

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

}
