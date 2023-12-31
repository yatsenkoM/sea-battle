package com.project.seaBattle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Cell(val imageRect: Rect, private val isHit: Boolean, context: Context) {
    private var bitmapMiss: Bitmap? = null
    private var bitmapHit: Bitmap? = null

    init {
        bitmapMiss = BitmapFactory.decodeResource(context.resources, R.drawable.miss)
        bitmapHit = BitmapFactory.decodeResource(context.resources, R.drawable.hit)
    }

    private fun drawMiss(canvas: Canvas) {
        bitmapMiss?.let {
            imageRect.let { rect ->
                canvas.drawBitmap(it, null, rect, null)
            }
        }
    }

    private fun drawHit(canvas: Canvas) {
        bitmapHit?.let {
            imageRect.let { rect ->
                canvas.drawBitmap(it, null, rect, null)
            }
        }
    }

    fun drawClick(canvas: Canvas) {
        if(isHit)
            drawHit(canvas)
        else
            drawMiss(canvas)
    }
}