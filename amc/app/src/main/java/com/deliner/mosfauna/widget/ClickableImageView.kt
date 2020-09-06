package com.deliner.mosfauna.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.deliner.mosfauna.system.CoreConst
import com.deliner.mosfauna.utils.StaticHandler
import java.util.*

class ClickableImageView : AppCompatImageView, View.OnTouchListener {

    constructor(context: Context, attrs: AttributeSet, attrSet: Int) : super(
        context,
        attrs,
        attrSet
    )

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context)

    private lateinit var bitmap: Bitmap

    init {
        isDrawingCacheEnabled = true
    }


    private lateinit var handler: StaticHandler
    private var imageId: Int? = null
    private var ignore = false

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setOnTouchListener(this)
    }

    fun setHandler(handler: StaticHandler): ClickableImageView {
        this.handler = handler
        return this
    }

    fun setImageId(id: Int): ClickableImageView {
        this.imageId = id
        return this
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        bitmap = bm
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (ignore) return false
        ignore = true

        Timer().schedule(object : TimerTask() {
            override fun run() {
                ignore = false
            }
        }, 1000)

        val bmp = Bitmap.createBitmap(v.drawingCache)

        val color = bmp!!.getPixel(event.x.toInt(), event.y.toInt())
        if (color != Color.TRANSPARENT) {
            Message.obtain(handler, CoreConst.ON_IMAGE_CLICKED, imageId).sendToTarget()
        }
        return color != Color.TRANSPARENT
    }
}