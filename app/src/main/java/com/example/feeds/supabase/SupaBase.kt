package com.example.feeds.supabase

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feeds.ChatScreen
import com.example.feeds.R
import com.example.feeds.constants.Secrets
import com.example.feeds.adapters.ChatAdapter
import com.example.feeds.models.ChatModel
import com.example.feeds.models.MessageModel
import com.example.feeds.models.UserModel
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.handleDeeplinks
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.runBlocking

val secrets: Secrets = Secrets()
val supabase = createSupabaseClient(
    supabaseUrl = secrets.getSupabaseUrl(),
    supabaseKey = secrets.getSupabaseKey(),
) {
    install(Postgrest)
    install(Auth)
}
class SupaBase{

    fun handleDeepLink(intent: Intent) {
        supabase.handleDeeplinks(
            intent = intent,
            onSessionSuccess = {
                userSession -> userSession.user?.id
                println("\n\nTHE REGISTERED USER: ${userSession.user?.id}\n\n")
            }
        )
    }

    fun addNewUser() {
        runBlocking {
            try {
                val user =  newUser()
                println("\n\nTHE NEW USER: $user\n\n")

            } catch (e: Exception) {
                println("AN ERROR OCCURRED: $e")
            }
        }
    }

    private val user = UserModel("funky101", "nusetorsetsofia101@gmail.com", "password", "0555159963")
    private suspend fun newUser() {
        val response = supabase.auth.signUpWith(Email){
            email = user.getEmail()
            password = user.getPassword()
        }

        println("USER ADDED SUCCESSFULLY: $response")
    }

    private suspend fun addUserName(id: String, username: String) {
        supabase.from("profiles")
            .insert(listOf(
                id,
                username
            ))
            .decodeList<String>()
    }

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