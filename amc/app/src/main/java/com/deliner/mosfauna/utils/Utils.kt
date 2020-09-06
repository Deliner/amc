package com.deliner.mosfauna.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object Utils {

    /**
     * Use this method only to send mail, no simple sharing to social apps or something else
     */
    fun shareTextToEmail(context: Context, email: Array<String>, subject: String, text: String) {
        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.data = Uri.parse("mailto:")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, text)
        emailIntent.selector = selectorIntent
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Отправить письмо"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Нет поддерживаемых приложений", Toast.LENGTH_SHORT).show()
        }
    }
}