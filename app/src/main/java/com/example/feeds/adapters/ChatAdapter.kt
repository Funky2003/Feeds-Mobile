package com.example.feeds.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.feeds.R
import com.example.feeds.models.ChatModel

//mList = message list
class ChatAdapter ( val messages: MutableList<ChatModel>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_bubble_component, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.chats)

        fun bind(chatModel: ChatModel) {
            messageTextView.text = chatModel.message

            val params = messageTextView.layoutParams as RelativeLayout.LayoutParams
            if (chatModel.isSent) {
                params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
                params.removeRule(RelativeLayout.ALIGN_PARENT_START)
                messageTextView.setBackgroundResource(R.drawable.chat_bubble_background_sent)
            } else {
                params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE)
                params.removeRule(RelativeLayout.ALIGN_PARENT_END)
                messageTextView.setBackgroundResource(R.drawable.chat_bubble_background_received)
            }
            messageTextView.layoutParams = params
        }
    }
}