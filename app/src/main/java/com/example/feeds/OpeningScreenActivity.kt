package com.example.feeds

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feeds.sharedpreferences.SharedPreferences

class OpeningScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_opening_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        val sharedPreferences = SharedPreferences()
        if (sharedPreferences.isUserLoggedInBefore(this)) {
            proceedToMainActivity()
        }
        super.onStart()
    }

    fun goToSignUpScreen(view: View) {
        val signUpActivity = Intent(view.context, SignUpActivity::class.java)
        startActivity(signUpActivity)
    }

    fun goToSignInScreen(view: View) {
        val signInIntent = Intent(view.context, SignInActivity::class.java)
        startActivity(signInIntent)
    }

    private fun proceedToMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }
}