package com.deliner.mosfauna.activity

import android.os.Bundle
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.deliner.mosfauna.dialog.CommonDialogFragment
import com.deliner.mosfauna.dialog.DialogManager
import com.deliner.mosfauna.utils.StaticHandler
import java.lang.Exception

abstract class CommonActivity : AppCompatActivity(), StaticHandler.Callback, DialogManager.Callback {

    private lateinit var dialogManager: DialogManager

    override fun handleServiceMessage(msg: Message) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogManager = DialogManager(this)
    }

    override fun onPause() {
        dialogManager.onPause()
        super.onPause()
    }

    override fun onPostResume() {
        dialogManager.onPostResume()
        super.onPostResume()
    }

    override fun getSupportManager(): FragmentManager {
        return supportFragmentManager
    }

    override fun onCreateDialogEx(tag: String, args: Bundle?): CommonDialogFragment {
        throw Exception("No dialog implementation for this tag $tag")
    }

    protected fun showDialogEx(tag: String, args: Bundle? = null) = dialogManager.showDialogEx(tag, args)
}
