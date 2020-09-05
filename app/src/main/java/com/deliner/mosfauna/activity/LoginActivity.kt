package com.deliner.mosfauna.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.deliner.mosfauna.R
import com.deliner.mosfauna.utils.LoginManager

class LoginActivity : UserActivity() {

    private lateinit var passwordText: EditText
    private lateinit var loginText: EditText

    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loginButton = findViewById(R.id.activity_login_button_login)
        passwordText = findViewById(R.id.activity_login_password)
        loginText = findViewById(R.id.activity_login_login)

        loginButton.setOnClickListener {
            if (checkLogin(loginText.text.toString()) && checkPassword(passwordText.text.toString())) {
                val user = LoginManager.User(
                    loginText.text.toString(),
                    passwordText.text.toString(),
                    "default@gmail.com"
                )
                LoginManager.getInstance(applicationContext).setCurrentUser(user)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, GreetActivity::class.java))
    }
}