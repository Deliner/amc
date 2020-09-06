package com.deliner.mosfauna.dialog

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import java.util.concurrent.ConcurrentLinkedQueue

class DialogManager(private val callback: Callback) {

    private val dialogQueue = ConcurrentLinkedQueue<DialogData>()
    private var paused = true

    fun showDialogEx(tag: String, args: Bundle? = null){
        if (paused){
            dialogQueue.add(DialogData(tag, args))
        } else if (callback.getSupportManager().findFragmentByTag(tag) == null) {
            val dialog = callback.onCreateDialogEx(tag, args)
            showCommonDialog(tag, dialog)
        }
    }

    private fun showCommonDialog(tag: String, dialog: CommonDialogFragment){
        dialog.show(callback.getSupportManager(), tag)
    }

    fun onPause(){
        paused = true
    }

    fun onPostResume() {
        paused = false
        while (!dialogQueue.isEmpty()) {
            val data = dialogQueue.poll()
            if (data != null){
                showDialogEx(data.tag, data.args)
            }
        }
    }

    data class DialogData(val tag: String, val args: Bundle? = null)

    interface Callback{
        fun onCreateDialogEx(tag: String, args: Bundle? = null): CommonDialogFragment
        fun getSupportManager(): FragmentManager
    }
}