package com.deliner.mosfauna.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.deliner.mosfauna.R
import com.deliner.mosfauna.utils.LoginManager

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

        if (LoginManager.getInstance(applicationContext).getCurrentUser() != null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, GreetActivity::class.java))
        }
    }
}