package com.deliner.mosfauna.utils

import android.content.Context
import android.content.SharedPreferences
import com.deliner.mosfauna.R
import java.lang.StringBuilder

class LoginManager private constructor(private val context: Context) {

    var user: User? = null

    fun getCurrentUser(): User? {
        if (user != null) {
            return user
        }

        val prefs: SharedPreferences = context.getSharedPreferences(
            context.getString(R.string.pref_login_manger),
            Context.MODE_PRIVATE
        )
        val data: String? = prefs.getString(KEY_USER, null)
        data?.let {
            return try {
                user = User.fromString(data)
                user
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    fun setCurrentUser(user: User) {
        val pref = context.getSharedPreferences(
            context.getString(R.string.pref_login_manger),
            Context.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putString(KEY_USER, user.toString())
        editor.apply()

        this.user = user
    }

    data class User(val login: String, val password: String, val mail: String) {

        override fun toString(): String {
            val builder = StringBuilder()
            builder.append(login).append("\n").append(password).append("\n").append(mail)
            return builder.toString()
        }

        companion object {
            @Throws
            fun fromString(data: String): User {
                val array = data.split("\n")
                if (array.size != 3) {
                    throw Exception("wrong data for User")
                } else {
                    return User(array[0], array[1], array[2])
                }
            }
        }
    }

    companion object {
        private const val KEY_USER = "key_user"

        private var manger: LoginManager? = null
        private val lock = Object()

        @Synchronized
        fun getInstance(context: Context): LoginManager {
            synchronized(lock) {
                if (manger == null) {
                    manger = LoginManager(context)
                }
                return manger!!
            }
        }
    }
}