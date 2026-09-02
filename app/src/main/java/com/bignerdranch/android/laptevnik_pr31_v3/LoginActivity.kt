package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etLogin: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var ivTogglePassword: ImageView
    private lateinit var prefs: SharedPreferences

    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

        etLogin = findViewById(R.id.etLogin)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        ivTogglePassword = findViewById(R.id.ivTogglePassword)


        ivTogglePassword.setOnClickListener {
            passwordVisible = !passwordVisible
            if (passwordVisible) {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            etPassword.setSelection(etPassword.text.length)
        }

        btnLogin.setOnClickListener {
            val login = etLogin.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()) {
                showAlertDialog("Введите логин и пароль")
                return@setOnClickListener
            }

            val savedLogin = prefs.getString("login", null)
            val savedPassword = prefs.getString("password", null)

            if (savedLogin == null && savedPassword == null) {
                if (login == "ects" && password == "ects2025") {
                    prefs.edit()
                        .putString("login", login)
                        .putString("password", password)
                        .apply()
                    goToChats()
                } else {
                    showAlertDialog("Неверный логин или пароль")
                }
            } else {
                if (login == savedLogin && password == savedPassword) {
                    goToChats()
                } else {
                    showAlertDialog("Неверный логин или пароль")
                }
            }
        }
    }

    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun goToChats() {
        val intent = Intent(this, ChatsActivity::class.java)
        startActivity(intent)
    }
}
