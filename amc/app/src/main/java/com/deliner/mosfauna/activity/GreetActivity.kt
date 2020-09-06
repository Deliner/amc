package com.deliner.mosfauna.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.deliner.mosfauna.R
import com.deliner.mosfauna.utils.LoginManager

class GreetActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var joinButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greet)

        if (LoginManager.getInstance(applicationContext).getCurrentUser() != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(Intent(this, StartActivity::class.java))
            return
        }

        joinButton = findViewById(R.id.activity_greet_join)
        loginButton = findViewById(R.id.activity_greet_login)

        joinButton.setOnClickListener { startActivity(Intent(this, JoinActivity::class.java)) }
        loginButton.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
    }
}