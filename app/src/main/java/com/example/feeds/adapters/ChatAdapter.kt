package com.example.feeds.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.feeds.R
import com.example.feeds.models.ChatModel

//mList = message list
class ChatAdapter (private val mLIst: List<ChatModel>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_bubble_component, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val items = mLIst[position]
        holder.chats.text = items.textMessage
    }

    override fun getItemCount(): Int {
        return mLIst.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chats: TextView = itemView.findViewById(R.id.chats)
    }
}