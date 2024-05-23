package com.example.feeds.validations

import android.graphics.Color
import android.widget.EditText
import android.widget.TextView
import com.example.feeds.R
import com.example.feeds.SignUpActivity

class Validation {
    fun validateSignupFields(view: SignUpActivity): Boolean {
        val username = view.findViewById<EditText>(R.id.username_editText).text.toString()
        val email = view.findViewById<EditText>(R.id.email_editText).text.toString()
        val password = view.findViewById<EditText>(R.id.password_editText).text.toString()
        val confirmPassword = view.findViewById<EditText>(R.id.confirm_password_editText).text.toString()

        val usernameLabel = view.findViewById<TextView>(R.id.username_label)
        val emailLabel = view.findViewById<TextView>(R.id.email_label)
        val passwordLabel = view.findViewById<TextView>(R.id.password_label)
        val confirmPasswordLabel = view.findViewById<TextView>(R.id.confirm_password_label)

        var isValid = true

        // Validate username
        if (username.isEmpty()) {
            usernameLabel.text = view.getString(R.string.field_cannot_be_empty)
            usernameLabel.setTextColor(Color.parseColor("#F54135"))
            isValid = false
        } else {
            usernameLabel.text = view.getString(R.string.username_start_upper)
            usernameLabel.setTextColor(Color.parseColor("#000000"))
        }

        // Validate email
        if (email.isEmpty()) {
            emailLabel.text = view.getString(R.string.field_cannot_be_empty)
            emailLabel.setTextColor(Color.parseColor("#F54135"))
            isValid = false
        } else {
            emailLabel.text = view.getString(R.string.email_start_upper)
            emailLabel.setTextColor(Color.parseColor("#000000"))
        }

        // Validate password
        if (password.isEmpty()) {
            passwordLabel.text = view.getString(R.string.field_cannot_be_empty)
            passwordLabel.setTextColor(Color.parseColor("#F54135"))
            isValid = false
        } else if (password.length < 8) {
            passwordLabel.text = view.getString(R.string.password_length_too_short_start_upper)
            passwordLabel.setTextColor(Color.parseColor("#F54135"))
            isValid = false
        } else {
            passwordLabel.text = view.getString(R.string.password_start_upper)
            passwordLabel.setTextColor(Color.parseColor("#000000"))
        }

        // Validate confirm password
        if (confirmPassword.isEmpty()) {
            confirmPasswordLabel.text = view.getString(R.string.field_cannot_be_empty)
            confirmPasswordLabel.setTextColor(Color.parseColor("#F54135"))
            isValid = false
        } else if (password != confirmPassword) {
            confirmPasswordLabel.text = view.getString(R.string.password_mismatch)
            confirmPasswordLabel.setTextColor(Color.parseColor("#F54135"))
            passwordLabel.text = view.getString(R.string.password_mismatch)
            passwordLabel.setTextColor(Color.parseColor("#F54135"))
            isValid = false
        } else {
            confirmPasswordLabel.text = view.getString(R.string.confirm_password_start_upper)
            confirmPasswordLabel.setTextColor(Color.parseColor("#000000"))
        }

        return isValid
    }
}