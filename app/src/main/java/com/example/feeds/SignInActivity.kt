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
import com.example.feeds.dtos.LoginDTO
import com.example.feeds.models.UserModel
import com.example.feeds.supabase.SupaBase
import kotlinx.coroutines.runBlocking

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun goToSignUpScreen(view: View) {
        val signUpActivity = Intent(this, SignUpActivity::class.java)
        startActivity(signUpActivity)
        finish()
    }

    fun signInUser(view: View) {
        val email = findViewById<EditText>(R.id.login_email_editText)
        val password = findViewById<EditText>(R.id.login_password_editText)


        val loginDTO = LoginDTO(
            email = email.text.toString(),
            password = password.text.toString()
        )
        val supaBase = SupaBase()
        runBlocking {
            showProgressHideButton()

            try {
                supaBase.loginUser(view, loginDTO)
                clearTextFields()

                val mainActivityIntent = Intent(view.context, MainActivity::class.java)
                startActivity(mainActivityIntent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(view.context, "Invalid email or password", Toast.LENGTH_LONG).show()

                hideProgressShowButton()
                println("ERROR SIGNING IN: ${e.message}")
            }
        }
    }

    private fun clearTextFields() {
        val email = findViewById<EditText>(R.id.login_email_editText)
        val password = findViewById<EditText>(R.id.login_password_editText)
        email.text.clear()
        password.text.clear()
    }

    private fun hideProgressShowButton() {
        val progressBar = findViewById<ProgressBar>(R.id.signin_progress_bar)
        val signInButton = findViewById<Button>(R.id.log_in_button)

        signInButton.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressHideButton() {
        val progressBar = findViewById<ProgressBar>(R.id.signin_progress_bar)
        val signInButton = findViewById<Button>(R.id.log_in_button)

        signInButton.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }
}