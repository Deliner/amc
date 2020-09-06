package com.deliner.mosfauna.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.deliner.mosfauna.R
import com.deliner.mosfauna.system.CoreConst


class RequestCodeDialog private constructor() : CommonDialogFragment() {

    private var success = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_request_code, container, false)
        val inputView = view.findViewById<EditText>(R.id.dialog_request_code_input)

        view.findViewById<Button>(R.id.dialog_request_code_send).setOnClickListener {
            success = true
            Message.obtain(callback, CoreConst.ON_SEND_REQUEST_CODE, inputView.text.toString()).sendToTarget()
            dismiss()
        }
        view.findViewById<Button>(R.id.dialog_request_code_cancel).setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!success) {
            Message.obtain(callback, CoreConst.ON_CANCEL_REQUEST_CODE).sendToTarget()
        }
    }

    companion object {
        fun getInstance(): RequestCodeDialog {
            return RequestCodeDialog()
        }
    }
}