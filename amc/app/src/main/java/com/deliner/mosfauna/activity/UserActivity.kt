package com.deliner.mosfauna.activity

import android.text.TextUtils
import android.widget.Toast

abstract class UserActivity : CommonActivity() {

    protected fun checkEmail(email: String): Boolean {
        return if (email.isEmailValid()) {
            true
        } else {
            Toast.makeText(applicationContext, "Wrong email format", Toast.LENGTH_SHORT).show()
            false
        }
    }

    protected fun checkLogin(login: String): Boolean {
        return if (login.isAlphaNumSpace()) {
            return if (login.length <= 32) {
                true
            } else {
                Toast.makeText(applicationContext, "Login should be less than 32 characters ", Toast.LENGTH_SHORT).show()
                false
            }
        } else {
            Toast.makeText(applicationContext, "Login should be alpha or numeric", Toast.LENGTH_SHORT).show()
            false
        }
    }

    protected fun checkPassword(password: String): Boolean {
        return if (password.matches("""[\w]+""".toRegex())) {
            return if (password.length <= 32) {
                true
            } else {
                Toast.makeText(applicationContext, "Password should be less than 32 characters ", Toast.LENGTH_SHORT).show()
                false
            }
        } else {
            Toast.makeText(applicationContext, "Password should be alpha or numeric", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun String.isAlphaNumSpace(): Boolean {
        val reg = """[\w| ]+""".toRegex()
        return this.matches(reg)
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}