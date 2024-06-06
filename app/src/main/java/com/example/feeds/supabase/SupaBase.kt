package com.example.feeds.supabase

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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
import com.example.feeds.models.UserStatus
import com.example.feeds.network.Connectivity
import com.example.feeds.sharedpreferences.SharedPreferences
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.PresenceAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.presenceChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


val secrets: Secrets = Secrets()
val supabase = createSupabaseClient(
    supabaseUrl = secrets.getSupabaseUrl(),
    supabaseKey = secrets.getSupabaseKey()
) {
    install(Realtime)
    install(Postgrest)
    install(Auth)
    useHTTPS = true
}
class SupaBase {
    private val connectivity: Connectivity = Connectivity()
    private val sharedPreferences = SharedPreferences()
    fun getUser() : UserInfo? {
        var user: UserInfo?

        runBlocking {
            val result = supabase.auth.sessionManager.loadSession()?.user
            user = result
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


    //<--- get the subscription --->
    fun subscribeToIncomingMessages(context: ChatScreen) {

        context.lifecycleScope.launch(Dispatchers.IO) {
            if (connectivity.isNetworkAvailable(context)) {

                try {
                    val channel = supabase.realtime.channel("public:message")
                    val productChangeFlow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
                        table = "message"
                    }
                    channel.subscribe()

                    productChangeFlow.collect { message ->
                        val chatData = message.decodeRecord<ChatModel>()

                        withContext(Dispatchers.Main) {
                            context.chatAdapter?.messages?.add(chatData)
                            context.findViewById<RecyclerView>(R.id.chat_recyclerview).scrollToPosition(context.chatAdapter?.itemCount?.minus(1) ?: 0)
                        }
                    }
                } catch (e: Exception) {
                    println("\n\nAN ERROR CONNECTING TO THE WEBSOCKET: $e")
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Cannot print the live message", Toast.LENGTH_LONG).show()
                }
            }
        }
    }




    // track the user status
    fun trackUserStatus(context: Context) {
        val userId = getUser()?.id
        if (context is AppCompatActivity) {
            context.lifecycle.addObserver(object : DefaultLifecycleObserver{
                override fun onResume(owner: LifecycleOwner) {
                    updateUserStatus(userId, true, context)
                    super.onResume(owner)
                }

                override fun onStop(owner: LifecycleOwner) {
                    updateUserStatus(userId, false, context)
                    super.onStop(owner)
                }
            })

            try {
                getUserStatus(getUser()?.id, context)
            } catch (e: Exception) {
                println("Error tracking user status: $e")
            }
        }
    }
    private fun getUserStatus(userId: String?, context: Context) {
        if (context is AppCompatActivity) {
            context.lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = async {
                        supabase.from("user_status")
                            .select{
                                filter {
                                    UserStatus::id eq userId
                                }
                            }
                            .decodeList<UserStatus>()
                    }
                    val result = response.await()
                    if (result.isEmpty()) {
                        createUserStatus(userId, context)
                    }
                } catch (exception: Exception) {
                    println("ERROR GETTING STATUS: $exception")
                }
            }
        }
    }
    private fun createUserStatus(userId: String?, context: Context) {
        val userStatus = UserStatus(id = userId, true)
        if (context is AppCompatActivity) {
            context.lifecycleScope.launch(Dispatchers.IO) {
                val insertUserStatus = async {
                    supabase
                        .from("user_status")
                        .insert(listOf(userStatus))
                }

                val insertData = insertUserStatus.await()
                if (insertData.headers.isEmpty()) {
                    println("Error inserting user status: ${insertData.data}")
                } else {
                    println("User status added successfully")
                }
            }
        }
    }
    private fun updateUserStatus(userId: String?, online: Boolean, context: Context) {
        if (context is AppCompatActivity) {
            context.lifecycleScope.launch(Dispatchers.IO) {
                val updateResult = async {
                    supabase
                        .from("user_status")
                        .update(
                            {
                                UserStatus::online setTo online
                            }
                        ) {
                            filter {
                                UserStatus::id eq userId
                            }
                        }
                }

                val updateData = updateResult.await()
                println("User status updated successfully: ${updateData.data}")
            }
        }
    }
    fun isOnline(context: Context) {

        if (context is AppCompatActivity) {
            val senderId = context.intent.getStringExtra("senderId")
            context.lifecycleScope.launch(Dispatchers.IO) {
                if (connectivity.isNetworkAvailable(context)) {

                    try {
                        val channel = supabase.realtime.channel("public:user_status")
                        val productChangeFlow = channel.postgresChangeFlow<PostgresAction.Update>(schema = "public") {
                            table = "user_status"
                            filter("id", FilterOperator.EQ, senderId!!)
                        }
                        channel.subscribe()

                        productChangeFlow.collect { userStatus ->
                            val chatData = userStatus.decodeRecord<UserStatus>()

                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "User live status ${chatData.online}", Toast.LENGTH_LONG).show()
                                val onlineIcon = context.findViewById<RelativeLayout>(R.id.online_icon)
                                if (chatData.online) {
                                    onlineIcon.visibility = View.VISIBLE
                                } else {
                                    onlineIcon.visibility = View.GONE
                                }
                            }
                        }
                    } catch (e: Exception) {
                        println("\n\nAN ERROR CONNECTING TO STATUS THE WEBSOCKET: $e")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Cannot fetch live message", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }




    //<---- send the messages ---->
    fun setRecyclerView(context: ChatScreen) {
        val chatRecycler = context.findViewById<RecyclerView>(R.id.chat_recyclerview)
        val progressBar = context.findViewById<ProgressBar>(R.id.chat_progress_bar)
        val userName = context.findViewById<TextView>(R.id.header_username)
        val senderUserName = context.intent.getStringExtra("header_user_name")
        chatRecycler.layoutManager = LinearLayoutManager(context)
        val senderId = context.intent.getStringExtra("senderId")
        val user = getUser() // get the authenticated user id


        context.lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                // show the progress bar
                progressBar.visibility = ProgressBar.VISIBLE
            }

            if (connectivity.isNetworkAvailable(context)) {
//                val online = isOnline(senderId, context)
//                println("THE USER STATUS: $online")

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
                        userName.text = senderUserName
                        val adapter = ChatAdapter(data)
                        chatRecycler.adapter = adapter
                        chatRecycler.scrollToPosition(data.size - 1)
                        progressBar.visibility = ProgressBar.GONE

                        context.chatAdapter = adapter
//                        if (online) onlineIcon.visibility = View.VISIBLE else onlineIcon.visibility = View.GONE
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
                    message.await()

                    withContext(Dispatchers.Main) {
                        textMessage.text.clear()
                    }
                } catch (exception: Exception) {
                    println("ERROR SENDING MESSAGE: $exception")
                }
            }
        }
    }
}