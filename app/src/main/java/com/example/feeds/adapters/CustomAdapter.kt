package com.example.feeds.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.feeds.R
import com.example.feeds.models.ItemsViewModel

class CustomAdapter(private val mList: List<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_chat_component, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        holder.profileAvatar.setImageResource(itemsViewModel.profileAvatar)
        holder.unreadChatCount.text = itemsViewModel.unreadChatCount.toString()
        holder.username.text = itemsViewModel.username
        holder.textMessage.text = itemsViewModel.textMessage
        holder.textTime.text = itemsViewModel.textTime
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileAvatar: ImageView = itemView.findViewById(R.id.userProfile)
        val unreadChatCount: TextView = itemView.findViewById(R.id.msgCount)
        val username: TextView = itemView.findViewById(R.id.username)
        val textMessage: TextView = itemView.findViewById(R.id.txtMessage)
        val textTime: TextView = itemView.findViewById(R.id.txtTime)
    }
}