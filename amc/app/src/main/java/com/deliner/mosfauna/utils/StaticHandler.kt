package com.deliner.mosfauna.utils

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentLinkedQueue

class StaticHandler : Handler() {
    private val queue = ConcurrentLinkedQueue<Message>()

    private var callback: WeakReference<Callback>? = null
    private var queueMessages = true

    fun setCallback(callback: Callback) {
        this.callback = WeakReference(callback)
        while (queue.size > 0) {
            val msg = queue.poll()
            if (msg != null) {
                callback.handleServiceMessage(msg)
            }
        }
    }

    fun dropCallback(callback: Callback) {
        if (this.callback != null && this.callback!!.get() === callback) {
            this.callback = null
        }
    }

    override fun handleMessage(msg: Message) {
        if (callback == null || callback!!.get() == null || queueMessages && !queue.isEmpty()) {
            if (queueMessages) {
                val m = Message()
                m.copyFrom(msg)
                queue.add(m)
            }
        } else {
            callback?.get()?.handleServiceMessage(msg)
        }
    }

    interface Callback {
        fun handleServiceMessage(msg: Message)
    }
}