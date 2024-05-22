package com.example.feeds

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feeds.dtos.SignupDTO
import com.example.feeds.supabase.SupaBase
import kotlinx.coroutines.runBlocking

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun goToSignInScreen(view: View) {
        val signInIntent = Intent(this, SignInActivity::class.java)
        startActivity(signInIntent)
        finish()
    }

    fun addNewUser(view: View) {
        val signInActivity = SignInActivity()

        val username = findViewById<EditText>(R.id.username_editText)
        val email = findViewById<EditText>(R.id.email_editText)
        val password = findViewById<EditText>(R.id.password_editText)
        val confirmPassword = findViewById<EditText>(R.id.confirm_password_editText)
        val progressBar = findViewById<ProgressBar>(R.id.signup_progress_bar)
        val signUpButton = findViewById<Button>(R.id.create_account_button)

        val user = SignupDTO(
            username.text.toString(),
            email.text.toString(),
            password.text.toString(),
            confirmPassword.text.toString())
        val supaBase = SupaBase()

        clearTextFields() // clear the text fields
        runBlocking {
            try {
                signInActivity.showProgressHideButton(progressBar = progressBar, button = signUpButton)
                supaBase.newUser(view, user)
            } catch (e: Exception) {
                Toast.makeText(view.context, "Cannot register account, try again later.", Toast.LENGTH_LONG).show()

                signInActivity.hideProgressShowButton(progressBar = progressBar, button = signUpButton)
                println("AN ERROR OCCURRED: $e")
            }
        }
    }

    private fun clearTextFields() {
        val username = findViewById<EditText>(R.id.username_editText)
        val email = findViewById<EditText>(R.id.email_editText)
        val password = findViewById<EditText>(R.id.password_editText)
        val confirmPassword = findViewById<EditText>(R.id.confirm_password_editText)

        username.text.clear()
        email.text.clear()
        password.text.clear()
        confirmPassword.text.clear()
    }
}