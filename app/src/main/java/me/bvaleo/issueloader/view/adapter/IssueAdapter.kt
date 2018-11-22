package me.bvaleo.issueloader.view.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import me.bvaleo.issueloader.R
import me.bvaleo.issueloader.model.Issue

class IssueAdapter(private val items: MutableList<Issue>) : RecyclerView.Adapter<IssueAdapter.ViewHolder>() {

    private companion object {
        const val OPEN = "open"
        const val CLOSED = "closed"
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val title = view.findViewById<TextView>(R.id.tv_title)
        val avatar = view.findViewById<ImageView>(R.id.iv_logo_author)
        val comment = view.findViewById<TextView>(R.id.tv_comment)
        val state = view.findViewById<TextView>(R.id.tv_state)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.issue_row, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = if(items[position].title.length > 75) "${items[position].title.substring(0, 75)}..." else items[position].title
        holder.comment.text = if (items[position].comments != 0) "${items[position].comments} comments" else "No comments"


        val title = when (items[position].state) {
            CLOSED -> holder.title.context.getString(R.string.closed) to Color.GREEN
            OPEN -> holder.title.context.getString(R.string.open) to Color.RED
            //default api response
            else -> holder.title.context.getString(R.string.closed) to Color.GREEN
        }

        with(holder.state) {
            text = title.first
            setTextColor(title.second)
        }

        Glide.with(holder.view.context)
                .asBitmap()
                .load(items[position].user.avatarUrl)
                .into(holder.avatar)
    }

    fun setData(list: List<Issue>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

}