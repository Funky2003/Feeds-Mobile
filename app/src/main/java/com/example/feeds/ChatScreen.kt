package com.example.feeds

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feeds.supabase.SupaBase

class ChatScreen : AppCompatActivity() {
    private val supaBase = SupaBase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // get the username
        setUsernameAndAvatar()

        // call the recycler
        supaBase.setRecyclerView(this)

        // add new user
        supaBase.addNewUser()
    }

    private fun setUsernameAndAvatar() {
        val name = intent.getStringExtra("username")
        val profile = intent.getIntExtra("profile", 0)

        val nUsername = findViewById<TextView>(R.id.header_username)
        val profileAvatar = findViewById<ImageView>(R.id.header_avatar)

        nUsername.text = name
        profileAvatar.setImageResource(profile)
    }
}