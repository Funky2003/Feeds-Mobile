package com.example.feeds.supabase

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feeds.ChatScreen
import com.example.feeds.R
import com.example.feeds.Secrets
import com.example.feeds.adapters.ChatAdapter
import com.example.feeds.models.ChatModel
import com.example.feeds.models.MessageModel
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.runBlocking

val secrets: Secrets = Secrets()
val supabase = createSupabaseClient(
    supabaseUrl = secrets.getSupabaseUrl(),
    supabaseKey = secrets.getSupabaseKey()
) {
    install(Postgrest)
}
class SupaBase{

    private suspend fun showMessages() : List<MessageModel> {
        val messages = supabase.from("messages")
            .select()
            .decodeList<MessageModel>()
        return messages
    }

    fun setRecyclerView(view: ChatScreen) {
        val chatRecycler = view.findViewById<RecyclerView>(R.id.chat_recyclerview)
        chatRecycler.layoutManager = LinearLayoutManager(view)

        val data = ArrayList<ChatModel>()
        runBlocking {
            try {
                showMessages().forEach {
                    data.add(ChatModel(it.content))
                }
            } catch (e: Exception) {
                println("Error fetching data: $e")
            }
        }

        val adapter = ChatAdapter(data)
        chatRecycler.adapter = adapter
    }
}