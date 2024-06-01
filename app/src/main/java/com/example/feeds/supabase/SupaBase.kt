package com.example.feeds.supabase

import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feeds.ChatScreen
import com.example.feeds.MainActivity
import com.example.feeds.R
import com.example.feeds.adapters.ChatAdapter
import com.example.feeds.adapters.CustomAdapter
import com.example.feeds.constants.Secrets
import com.example.feeds.dtos.LoginDTO
import com.example.feeds.dtos.MessageDTO
import com.example.feeds.dtos.ProfileDTO
import com.example.feeds.dtos.SignupDTO
import com.example.feeds.models.ChatModel
import com.example.feeds.models.ItemsViewModel
import com.example.feeds.models.MessageModel
import com.example.feeds.network.Connectivity
import com.example.feeds.sharedpreferences.SharedPreferences
import com.example.feeds.utilities.Utilities
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


val secrets: Secrets = Secrets()
val supabase = createSupabaseClient(
    supabaseUrl = secrets.getSupabaseUrl(),
    supabaseKey = secrets.getSupabaseKey(),
) {
    install(Postgrest)
    install(Auth)
}
class SupaBase {
    fun getUser() : UserInfo? {
        var user: UserInfo?

        runBlocking {
            val result = supabase.auth.sessionManager.loadSession()?.user
            user = result
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


    private val connectivity: Connectivity = Connectivity()
    suspend fun showUsers(appContext: MainActivity) {
        val recyclerView = appContext.findViewById<RecyclerView>(R.id.recyclerview)
        val progressBar = appContext.findViewById<ProgressBar>(R.id.homepage_progress_bar)

        recyclerView.layoutManager = LinearLayoutManager(appContext)
        var output: ArrayList<MessageModel>

        appContext.lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                progressBar.visibility = ProgressBar.VISIBLE
            }

            if (connectivity.isNetworkAvailable(appContext)) {
                try {
                    val result = async {
                        supabase.from("profiles")
                            .select()
                            .decodeAs<ArrayList<MessageModel>>()
                    }
                    output = result.await()

                    // Convert fetched data to ItemsViewModel list
                    val data = output.map {
                        ItemsViewModel(
                            profileAvatar = R.drawable.funky_avatar,
                            unreadChatCount = 4,
                            username = it.username,
                            textMessage = it.username,
                            textTime = "3 secs",
                            senderId = it.user_id
                        )
                    }

                    withContext(Dispatchers.Main) {
                        val adapter = CustomAdapter(data, appContext)
                        recyclerView.adapter = adapter
                        progressBar.visibility = ProgressBar.GONE
                    }
                } catch (e: Exception) {
                    println(e.stackTrace)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(appContext, "Failed to fetch data. Check your internet connection.", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(appContext, "No internet connection available.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    fun setRecyclerView(context: ChatScreen) {
        val chatRecycler = context.findViewById<RecyclerView>(R.id.chat_recyclerview)
        val progressBar = context.findViewById<ProgressBar>(R.id.chat_progress_bar)
        val userName = context.findViewById<TextView>(R.id.header_username)
        val senderId = context.intent.getStringExtra("senderId")
        val senderUserName = context.intent.getStringExtra("header_user_name")
        chatRecycler.layoutManager = LinearLayoutManager(context)
        val user = getUser() // get the authenticated user id

        context.lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                // show the progress bar
                progressBar.visibility = ProgressBar.VISIBLE
            }

            if (connectivity.isNetworkAvailable(context)) {
                try {
                    val result = async {
                        supabase.from("message")
                            .select{
                                filter {
                                    or {
                                        and {
                                            eq("sender_id", user?.id!!)
                                            eq("receiver_id", senderId!!)
                                        }
                                        and {
                                            eq("sender_id", senderId!!)
                                            eq("receiver_id", user?.id!!)
                                        }
                                    }
                                }
                            }
                            .decodeAs<ArrayList<ChatModel>>()
                    }
                    val fetchedData = result.await()
                    println("THE MESSAGE: $fetchedData")

                    val data = fetchedData.map {
                        ChatModel(
                            id = it.id,
                            created_at = it.created_at,
                            sender_id = it.sender_id,
                            receiver_id = it.receiver_id,
                            isSent = it.isSent,
                            message = it.message
                        )
                    }.toMutableList()

                    withContext(Dispatchers.Main) {
                        userName.text = senderUserName // set username
                        val adapter = ChatAdapter(data)
                        chatRecycler.adapter = adapter
                        progressBar.visibility = ProgressBar.GONE // hide progress bar

                        context.chatAdapter = adapter
                    }
                } catch (e: Exception) {
                    println(e.stackTrace)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "No internet connection available.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private val utilities = Utilities()
    fun sendMessage(context: ChatScreen) {
        val textMessage = context.findViewById<EditText>(R.id.chat_editText)
        val receiverId = context.intent.getStringExtra("senderId")
        val sender = getUser()

        val messageDTO = MessageDTO(
            sender_id = sender?.id!!,
            receiver_id = receiverId!!,
            isSent = true,
            message = textMessage.text.toString()
        )
        context.lifecycleScope.launch {
            if (!connectivity.isNetworkAvailable(context)) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "No internet connection available.", Toast.LENGTH_LONG).show()
                }
            }

            withContext(Dispatchers.IO) {
                try {
                    val message = async {
                        supabase.from("message")
                            .insert(
                                listOf(
                                    messageDTO
                                )
                            )
                    }
                    val result = message.await()
                    println("THE RESULTS: ${result.data}")

                    val insertedMessage = ChatModel(
                        id = utilities.generateUniqueId(),
                        created_at = utilities.getCurrentTimeStamp(),
                        sender_id = sender.id,
                        receiver_id = receiverId,
                        isSent = true,
                        message = messageDTO.message
                    )

                    withContext(Dispatchers.Main) {
                        context.chatAdapter?.messages?.add(insertedMessage)
                        textMessage.text.clear()

                        context.findViewById<RecyclerView>(R.id.chat_recyclerview).scrollToPosition(context.chatAdapter?.itemCount?.minus(1) ?: 0)
                    }
                } catch (exception: Exception) {
                    println("ERROR SENDING MESSAGE: $exception")
                }
            }
        }
    }
}