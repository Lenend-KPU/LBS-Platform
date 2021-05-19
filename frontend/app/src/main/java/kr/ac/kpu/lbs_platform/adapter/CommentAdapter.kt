package kr.ac.kpu.lbs_platform.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.poko.remote.Comment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommentAdapter(private val comments: Array<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)

        return CommentAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        val dateString = comments[position].fields.comment_date
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val dateTime: LocalDateTime = LocalDateTime.parse(dateString, formatter)
        holder.commentDateTextView.text = dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
        holder.commentTextTextView.text = comments[position].fields.comment_text
    }

    override fun getItemCount(): Int = comments.size
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val commentDateTextView: TextView = view.findViewById(R.id.commentDateTextView)
        val commentTextTextView: TextView = view.findViewById(R.id.commentTextTextView)
    }
}