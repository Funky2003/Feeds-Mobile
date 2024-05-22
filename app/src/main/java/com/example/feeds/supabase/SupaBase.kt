package com.example.feeds.supabase

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feeds.ChatScreen
import com.example.feeds.R
import com.example.feeds.adapters.ChatAdapter
import com.example.feeds.constants.Secrets
import com.example.feeds.dtos.LoginDTO
import com.example.feeds.models.ChatModel
import com.example.feeds.models.MessageModel
import com.example.feeds.dtos.SignupDTO
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
class SupaBase {

    fun handleDeepLink(intent: Intent) {
        supabase.handleDeeplinks(
            intent = intent,
            onSessionSuccess = {
                userSession -> userSession.user?.id
                println("\n\nTHE REGISTERED USER: ${userSession.user?.id}\n\n")
            }
        )
    }


    //<-- Create a new user -->
    suspend fun newUser(view: View, user: SignupDTO) {
        println("THE USER DATA: ${user.getUsername()}, ${user.getEmail()}, ${user.getPassword()}, ${user.getConfirmPassword()}")
        val response = supabase.auth.signUpWith(Email){
            email = user.getEmail()
            password = user.getPassword()
        }

        Toast.makeText(view.context, "Account created!\nConfirm your email 📧", Toast.LENGTH_LONG).show()
        println("USER ADDED SUCCESSFULLY: ${response?.confirmedAt}")
    }

    private suspend fun addUserName(id: String, username: String) {
        supabase.from("profiles")
            .insert(listOf(
                id,
                username
            ))
            .decodeList<String>()
    }

    //<-- Login user -->
    suspend fun loginUser(view: View, loginDTO: LoginDTO) {
        supabase.auth.signInWith(Email) {
            email = loginDTO.getEmail()
            password = loginDTO.getPassword()
        }

        Toast.makeText(view.context, "Login successful!", Toast.LENGTH_LONG).show()
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