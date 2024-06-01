package com.example.feeds

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feeds.sharedpreferences.SharedPreferences
import com.example.feeds.supabase.SupaBase
import pl.droidsonroids.gif.GifImageView

class DeepLinkHandlerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_deep_link_handler)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        val openingScreenActivity = Intent(this, OpeningScreen::class.java)
        startActivity(openingScreenActivity)
        finish()
        super.onBackPressed()
    }

    override fun onStart() {
        val supaBase = SupaBase()
        val sharedPreferences = SharedPreferences()

        val data: Uri? = intent.data
        if (data != null) {
            val token = data.getQueryParameter("token")
            val type = data.getQueryParameter("type")
            val redirectTo = data.getQueryParameter("redirect_to")

            if (supaBase.getUser()?.aud == "authenticated") {
                println("DeepLinkHandler Token: $token, Type: $type, Redirect To: $redirectTo")

                if (sharedPreferences.isShowSuccessGifBefore(this)) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        proceedToMainActivity()
                    }, 1)
                } else {
                    showSuccessGif()
                    Toast.makeText(this, "Account verified!", Toast.LENGTH_SHORT).show()
                    sharedPreferences.showSuccessGifState(this, true)

                    Handler(Looper.getMainLooper()).postDelayed({
                        proceedToMainActivity()
                    }, 2000)
                }
            } else {
                showFailureGif()
                Toast.makeText(this, "Account verification failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            println("DeepLinkHandler No data found in the intent")
        }
        super.onStart()
    }

    private fun proceedToMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

    private fun showSuccessGif() {
        val gifImageView = findViewById<GifImageView>(R.id.account_verified_gif)
        gifImageView.visibility = View.VISIBLE
    }

    private fun showFailureGif() {
        val gifImageView = findViewById<GifImageView>(R.id.account_verification_failed_gif)
        gifImageView.visibility = View.VISIBLE
    }
}