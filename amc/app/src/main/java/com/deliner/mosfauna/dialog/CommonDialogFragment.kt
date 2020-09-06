package com.deliner.mosfauna.dialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.deliner.mosfauna.utils.StaticHandler

open class CommonDialogFragment : AppCompatDialogFragment() {
    protected var callback: StaticHandler? = null
    protected var useCallback = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroyView() {
        if (dialog != null && retainInstance) dialog!!.setDismissMessage(null)
        super.onDestroyView()
    }

    fun setDialogResult(callback: StaticHandler?): CommonDialogFragment {
        this.callback = callback
        return this
    }

    override fun onResume() {
        super.onResume()
        if (callback == null && useCallback) {
            dismiss()
        }
    }
}