package com.example.feeds

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feeds.adapters.ChatAdapter
import com.example.feeds.supabase.SupaBase

class ChatScreen : AppCompatActivity() {
    private val supaBase = SupaBase()
    var chatAdapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // subscribe to incoming messages
        supaBase.subScribeToIncomingMessages(this@ChatScreen)

        // get the username
        setUsernameAndAvatar()

        // call the recycler
        supaBase.setRecyclerView(this)
    }



    private fun setUsernameAndAvatar() {
        val name = intent.getStringExtra("username")
        val profile = intent.getIntExtra("profile", 0)

        val nUsername = findViewById<TextView>(R.id.header_username)
        val profileAvatar = findViewById<ImageView>(R.id.header_avatar)

        nUsername.text = name
        profileAvatar.setImageResource(profile)
    }

    fun sendMessage(view: View) {
        supaBase.sendMessage(this@ChatScreen)
    }
}