package com.project.seaBattle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DrawingBoard(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint()
    private var rightBoard: GameBoard? = null
    private var leftBoard: GameBoard? = null

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
    }

    fun setBoard(board: GameBoard) {
        if (board.side == BoardSide.RIGHT) {
            rightBoard = board
        } else {
            leftBoard = board
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (rightBoard != null) {
            rightBoard?.setBoardLimits(context)
            rightBoard?.drawBoard(canvas, paint)
        }
        if (leftBoard != null) {
            leftBoard?.setBoardLimits(context)
            leftBoard?.drawBoard(canvas, paint)
        }
    }
}
