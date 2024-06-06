package com.example.feeds.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.feeds.R
import com.example.feeds.models.ChatModel
import com.example.feeds.supabase.SupaBase
import io.ktor.http.CacheControl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        private val timeSent: TextView = itemView.findViewById(R.id.time_sent)
        private val supaBase = SupaBase()
        fun bind(chatModel: ChatModel) {
            val fullTimeStamp = chatModel.created_at
            val fullFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())

            val date: Date? = fullFormat.parse(fullTimeStamp)
            val sentTime = date?.let { timeFormat.format(it) }
            messageTextView.text = chatModel.message
            timeSent.text = sentTime

            val params = messageTextView.layoutParams as RelativeLayout.LayoutParams
            val timeParam = timeSent.layoutParams as RelativeLayout.LayoutParams
            if (chatModel.sender_id == supaBase.getUser()?.id) {
                params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
                params.removeRule(RelativeLayout.ALIGN_PARENT_START)

                timeParam.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
                timeParam.removeRule(RelativeLayout.ALIGN_PARENT_START)
                messageTextView.setBackgroundResource(R.drawable.chat_bubble_background_sent)
            } else {
                params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE)
                params.removeRule(RelativeLayout.ALIGN_PARENT_END)

                timeParam.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE)
                timeParam.removeRule(RelativeLayout.ALIGN_PARENT_END)
                messageTextView.setBackgroundResource(R.drawable.chat_bubble_background_received)
            }
            messageTextView.layoutParams = params
        }
    }
}