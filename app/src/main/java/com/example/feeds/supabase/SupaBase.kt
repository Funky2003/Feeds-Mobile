package com.example.feeds.supabase

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feeds.ChatScreen
import com.example.feeds.R
import com.example.feeds.adapters.ChatAdapter
import com.example.feeds.constants.Secrets
import com.example.feeds.dtos.LoginDTO
import com.example.feeds.dtos.ProfileDTO
import com.example.feeds.dtos.SignupDTO
import com.example.feeds.models.ChatModel
import com.example.feeds.models.MessageModel
import com.example.feeds.sharedpreferences.SharedPreferences
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
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
    fun getUser() : String {
        var user: String

        runBlocking {
            val result = supabase.auth.sessionManager.loadSession()?.user
            user = result?.aud.toString()
            println("THE CURRENT USER: ${result?.aud}")
        }
        return user
    }

    //<-- Create a new user -->
    suspend fun createNewUser(view: View, user: SignupDTO) {
        try {
            val response = supabase.auth.signUpWith(Email) {
                email = user.getEmail()
                password = user.getPassword()
            }

            if (response?.id != null) {
                val profileDTO = ProfileDTO(response.id, user.getUsername())
                addUserName(view = view, profileDTO = profileDTO)
                sharedPreferences.showSuccessGifState(view.context, false)
                Toast.makeText(view.context, "Account created!\nConfirm your email 📧", Toast.LENGTH_LONG).show()
            } else {
                throw Exception("Sign-up failed: No user ID returned.")
            }
        } catch (e: Exception) {
            Toast.makeText(view.context, "Cannot register account, try again later.", Toast.LENGTH_LONG).show()
            println("AN ERROR OCCURRED: $e")
        }
    }

    private val sharedPreferences = SharedPreferences()
    private suspend fun addUserName(view: View, profileDTO: ProfileDTO) {
        try {
            val response: PostgrestResult = supabase.from("profiles")
                .insert(listOf(profileDTO))
            sharedPreferences.saveLoginState(view = view, loggedIn = true) // save the login state
            println("INSERTED DATA: ${response.data}")
        } catch (e: Exception) {
            println("AN ERROR OCCURRED DURING PROFILE INSERTION: $e")
        }
    }

    //<-- Login user -->
    suspend fun loginUser(view: View, loginDTO: LoginDTO) {
        supabase.auth.signInWith(Email) {
            email = loginDTO.getEmail()
            password = loginDTO.getPassword()
        }
        Toast.makeText(view.context, "Login successful!", Toast.LENGTH_LONG).show()
        sharedPreferences.saveLoginState(view = view, loggedIn = true) // save the login state
        sharedPreferences.showSuccessGifState(view.context, true)
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