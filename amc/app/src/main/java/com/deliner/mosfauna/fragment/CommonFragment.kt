package com.deliner.mosfauna.fragment

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.deliner.mosfauna.dialog.CommonDialogFragment
import com.deliner.mosfauna.dialog.DialogManager
import com.deliner.mosfauna.utils.StaticHandler

abstract class CommonFragment : Fragment(), StaticHandler.Callback, DialogManager.Callback  {

    private lateinit var dialogManager: DialogManager

    override fun handleServiceMessage(msg: Message) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        dialogManager = DialogManager(this)
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        dialogManager.onPause()
        super.onPause()
    }

    override fun onResume() {
        dialogManager.onPostResume()
        super.onResume()
    }

    override fun getSupportManager(): FragmentManager {
        return requireActivity().supportFragmentManager
    }

    override fun onCreateDialogEx(tag: String, args: Bundle?): CommonDialogFragment {
        throw Exception("No dialog implementation for this tag $tag")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResource(), container, false)
    }

    abstract fun getLayoutResource(): Int

    protected fun showDialogEx(tag: String, args: Bundle? = null) = dialogManager.showDialogEx(tag, args)
}