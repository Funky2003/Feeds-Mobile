package com.example.feeds

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ArithmeticOperationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_arithmetic_operations)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun areInputsValid(num1: String, num2: String, result: TextView): Boolean {
        if (num1.isEmpty() || num2.isEmpty()) {
            result.text = "Please enter both numbers."
            result.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun multiplyNum(view: View) {
        val num1 = findViewById<EditText>(R.id.num1_editText).text.toString()
        val num2 = findViewById<EditText>(R.id.num2_editText).text.toString()
        val result = findViewById<TextView>(R.id.txt_math_result)

        if (areInputsValid(num1, num2, result)) {
            val product = num1.toInt() * num2.toInt()
            result.text = product.toString()
            result.visibility = View.VISIBLE
        }
    }

    fun moduloNum(view: View) {
        val num1 = findViewById<EditText>(R.id.num1_editText).text.toString()
        val num2 = findViewById<EditText>(R.id.num2_editText).text.toString()
        val result = findViewById<TextView>(R.id.txt_math_result)

        if (areInputsValid(num1, num2, result)) {
            val modulo = num1.toInt() % num2.toInt()
            result.text = modulo.toString()
            result.visibility = View.VISIBLE
        }
    }

    fun divideNum(view: View) {
        val num1 = findViewById<EditText>(R.id.num1_editText).text.toString()
        val num2 = findViewById<EditText>(R.id.num2_editText).text.toString()
        val result = findViewById<TextView>(R.id.txt_math_result)

        if (areInputsValid(num1, num2, result)) {
            if (num2.toInt() == 0) {
                result.text = "Cannot divide by zero."
                result.visibility = View.VISIBLE
            } else {
                val quotient = num1.toInt() / num2.toInt()
                result.text = quotient.toString()
                result.visibility = View.VISIBLE
            }
        }
    }

    fun subtractNum(view: View) {
        val num1 = findViewById<EditText>(R.id.num1_editText).text.toString()
        val num2 = findViewById<EditText>(R.id.num2_editText).text.toString()
        val result = findViewById<TextView>(R.id.txt_math_result)

        if (areInputsValid(num1, num2, result)) {
            val difference = num1.toInt() - num2.toInt()
            result.text = difference.toString()
            result.visibility = View.VISIBLE
        }
    }

    fun addNum(view: View) {
        val num1 = findViewById<EditText>(R.id.num1_editText).text.toString()
        val num2 = findViewById<EditText>(R.id.num2_editText).text.toString()
        val result = findViewById<TextView>(R.id.txt_math_result)

        if (areInputsValid(num1, num2, result)) {
            val sum = num1.toInt() + num2.toInt()
            result.text = sum.toString()
            result.visibility = View.VISIBLE
        }
    }
}