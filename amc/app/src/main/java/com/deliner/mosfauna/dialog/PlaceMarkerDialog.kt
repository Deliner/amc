package com.deliner.mosfauna.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.deliner.mosfauna.R
import com.deliner.mosfauna.system.CoreConst


class PlaceMarkerDialog private constructor() : CommonDialogFragment() {

    private lateinit var name: String
    private var success = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_place_marker, container, false)
        view.findViewById<TextView>(R.id.dialog_place_marker_title).text =
            "Указать на ${name.toLowerCase()} на карте?"

        view.findViewById<Button>(R.id.dialog_request_code_send).setOnClickListener {
            success = true
            Message.obtain(callback, CoreConst.ON_SEND_MARKER, name).sendToTarget()
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
            Message.obtain(callback, CoreConst.ON_CANCEL_MARKER, name).sendToTarget()
        }
    }

    companion object {
        fun getInstance(name: String): PlaceMarkerDialog {
            val dialog = PlaceMarkerDialog()
            dialog.name = name
            return dialog
        }
    }
}