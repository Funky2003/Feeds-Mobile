package com.example.feeds

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.feeds.adapters.ChatAdapter
import com.example.feeds.models.MessageModel
import com.example.feeds.network.Connectivity
import com.example.feeds.supabase.SupaBase
import com.example.feeds.supabase.supabase
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.postgresListDataFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        supaBase.subscribeToIncomingMessages(this)

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