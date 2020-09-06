package com.deliner.mosfauna.activity

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.deliner.mosfauna.R
import com.deliner.mosfauna.dialog.CommonDialogFragment
import com.deliner.mosfauna.dialog.DialogTags
import com.deliner.mosfauna.dialog.RequestCodeDialog
import com.deliner.mosfauna.system.CoreConst
import com.deliner.mosfauna.utils.LoginManager
import com.deliner.mosfauna.utils.StaticHandler

class JoinActivity : UserActivity() {

    private lateinit var passwordText: EditText
    private lateinit var emailText: EditText
    private lateinit var loginText: EditText
    private lateinit var checkBox: CheckBox

    private lateinit var loginButton: Button

    private var pendingRequest = false

    private var user: LoginManager.User? = null

    override fun handleServiceMessage(msg: Message) {
        when (msg.what) {
            CoreConst.ON_SEND_REQUEST_CODE -> onSendRequestCode(msg.obj as String)
            CoreConst.ON_CANCEL_REQUEST_CODE -> pendingRequest = false
            else -> super.handleServiceMessage(msg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loginButton = findViewById(R.id.activity_join_button_join)
        passwordText = findViewById(R.id.activity_join_password)
        emailText = findViewById(R.id.activity_join_email)
        loginText = findViewById(R.id.activity_join_login)
        checkBox = findViewById(R.id.activity_join_checkbox)

        checkBox.isChecked = false
        loginButton.isEnabled = false
        checkBox.setOnClickListener { loginButton.isEnabled = (it as CheckBox).isChecked }

        loginButton.setOnClickListener {
            if (checkEmail(emailText.text.toString()) && checkLogin(loginText.text.toString()) && checkPassword(
                    passwordText.text.toString()
                ) && !pendingRequest
            ) {
                user = LoginManager.User(
                    loginText.text.toString(),
                    passwordText.text.toString(),
                    emailText.text.toString()
                )
                pendingRequest = true
                showDialogEx(DialogTags.REQUEST_CODE)
            }
        }
    }

    private fun onSendRequestCode(code: String) {
        if (code == "0000") {
            LoginManager.getInstance(applicationContext).setCurrentUser(user!!)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(applicationContext, "Invalid code format", Toast.LENGTH_SHORT).show()
            pendingRequest = false
        }
    }

    override fun onCreateDialogEx(tag: String, args: Bundle?): CommonDialogFragment {
        return when (tag) {
            DialogTags.REQUEST_CODE -> RequestCodeDialog.getInstance().setDialogResult(handler)
            else -> super.onCreateDialogEx(tag, args)
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

    override fun onPause() {
        handler.dropCallback(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        handler.setCallback(this)
    }

    companion object {
        private val handler = StaticHandler()
    }
}