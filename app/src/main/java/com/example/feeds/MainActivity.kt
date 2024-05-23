package com.example.feeds

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feeds.adapters.CustomAdapter
import com.example.feeds.models.HeaderItems
import com.example.feeds.models.ItemsViewModel
import com.example.feeds.supabase.SupaBase
import kotlin.random.Random.Default.nextInt

@Suppress("DEPRECATION")
class MainActivity(
    private var username: String = "Bra Funky❤️",
    private var avatar: Int = R.drawable.funky_avatar1,
) : AppCompatActivity() {
    private val supaBase = SupaBase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        supaBase.handleDeepLink(intent = intent) // handle the deeplink

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // handling the deeplink
        val supaBase = SupaBase()
        supaBase.handleDeepLink(intent = intent)

        // call the recycler
        setRecyclerView()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
            super.onBackPressed()
            return
    }

    private fun setRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val messages: List<String> = listOf(
            "Hey man, wassup 😁",
            "Charlie we have to meet the woman oo",
            "I will come see you on Monday",
            "Naa, maybe we should do it tonight",
            "This year de3 😂😂",
            "Awwwn 🤗🤗🥰🥰"
        )

        val names: List<String> = listOf(
            "Sammy 😎",
            "Carlie Jones",
            "Clara 🥰❤️",
            "Naa 😒",
            "Elinam 😇",
            "Mawunyo",
            "Thompson 😉😎😉"
        )

        val time: List<String> = listOf(
            "Yesterday",
            "12 mins",
            "Now",
            "Now",
            "1 Hour",
            "8 mins",
            "Now"
        )

        val avatars: List<Int> = listOf(
            R.drawable.funky_avatar,
            R.drawable.funky_avatar1,
            R.drawable.excelence_avatar,
            R.drawable.gwen_avatar,
            R.drawable.alison_avatar,
            R.drawable.sad_avatar,
            R.drawable.funky_avatar,
        )

        val data = ArrayList<ItemsViewModel>()
        for (i in 1..20) {
            data.add(
                ItemsViewModel(
                    avatars[nextInt(avatars.size)],
                    nextInt(i),
                    names[nextInt(names.size)],
                        messages[nextInt(messages.size)],
                        time[nextInt(time.size)]
                    ))
        }

        val adapter = CustomAdapter(data)
        recyclerView.adapter = adapter
    }

    fun goTOChatScreen(view: View) {
        val item = HeaderItems()
        item.setName(this.username)
        item.setProfileAvatar(this.avatar)

        val chatIntent = Intent(view.context, ChatScreen::class.java)

        chatIntent.putExtra("username", item.getName())
        chatIntent.putExtra("profile", item.getProfileAvatar())

        startActivity(chatIntent)
    }
}