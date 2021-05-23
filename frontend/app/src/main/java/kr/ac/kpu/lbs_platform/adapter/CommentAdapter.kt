package kr.ac.kpu.lbs_platform.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.fragment.invalidatable
import kr.ac.kpu.lbs_platform.global.Profile
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.poko.remote.Comment
import kr.ac.kpu.lbs_platform.poko.remote.ProfileRequest
import kr.ac.kpu.lbs_platform.poko.remote.ProfilesRequest
import kr.ac.kpu.lbs_platform.poko.remote.Request
import splitties.toast.toast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommentAdapter(private val comments: Array<Comment>, private val author: Int, private val fragment: invalidatable) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)

        return CommentAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        val profileId = comments[position].fields.profile
        getProfileFromServer(profileId) {
            holder.commentNameTextView.text = it.result!!.fields.profile_name
        }
        holder.commentTextTextView.text = comments[position].fields.comment_text

        val isMyComment = comments[position].fields.profile == Profile.profile!!.pk
        val submitButtonVisibility = if(isMyComment) View.VISIBLE else View.GONE

        holder.commentEditButton.visibility = submitButtonVisibility
        holder.commentEditLayout.visibility = View.GONE

        holder.commentEditButton.setOnClickListener {
            val oppositeCommentEditLayoutVisibility = if(holder.commentEditLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            holder.commentEditLayout.visibility = oppositeCommentEditLayoutVisibility
        }

        val authorId = author
        val documentId = comments[position].fields.document!!
        val commentId = comments[position].pk

        holder.submitEditButton.setOnClickListener {
            val content = holder.commentEditContentText.text.toString()
            if(content == "") {
                toast("변경할 내용이 없습니다.")
                return@setOnClickListener
            }
            editCommentToServer(authorId, documentId, commentId, content)
        }
        holder.submitDeleteButton.setOnClickListener {
            deleteCommentToServer(authorId, documentId, commentId)
        }
    }

    override fun getItemCount(): Int = comments.size
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val commentNameTextView: TextView = view.findViewById(R.id.commentNameTextView)
        val commentTextTextView: TextView = view.findViewById(R.id.commentTextTextView)
        val commentEditButton: Button = view.findViewById(R.id.commentEditButton)
        val commentEditLayout: LinearLayout = view.findViewById(R.id.commentEditLayout)
        val commentEditContentText: EditText = view.findViewById(R.id.commentEditContentText)
        val submitEditButton: Button = view.findViewById(R.id.submitEditButton)
        val submitDeleteButton: Button = view.findViewById(R.id.submitDeleteButton)
    }

    private fun getProfileFromServer(profileId: Int, callback: (ProfileRequest) -> Unit) {
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

    private fun editCommentToServer(profileId: Int, documentId: Int, commentId: Int, content: String) {
        val params = mutableMapOf<String, String>()
        params["text"] = content
        RequestHelper.Builder(Request::class)
            .apply {
                this.destFragment = null
                this.urlParameter = "profiles/$profileId/documents/$documentId/comments/$commentId/"
                this.method = com.android.volley.Request.Method.PUT
                this.onSuccessCallback = {
                    fragment.invalidateRecyclerView()
                    Log.i("EDIT", it.toString())
                }
                this.params = params
            }
            .build()
            .request()
    }
    private fun deleteCommentToServer(profileId: Int, documentId: Int, commentId: Int) {
        RequestHelper.Builder(Request::class)
            .apply {
                this.destFragment = null
                this.urlParameter = "profiles/$profileId/documents/$documentId/comments/$commentId/"
                this.method = com.android.volley.Request.Method.DELETE
                this.onSuccessCallback = {
                    fragment.invalidateRecyclerView()
                }
            }
            .build()
            .request()
    }
}