package com.project.seaBattle

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class BattleEditor(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val pressedCells: MutableList<Cell> = mutableListOf()
    private var rightBoard: GameBoard? = null
    private var leftBoard: GameBoard? = null
    private var currentGameBoard: GameBoard? = null
    var leftShips: MutableList<Ship>? = null
    var rightShips: MutableList<Ship>? = null
    private var currentShipsForCheck: MutableList<Ship>? = null
    private var lastDamagedShip: Ship? = null
    private var currentPlayerBitmap: Bitmap? = null
    private var currentPlayerImgRect: Rect? = null
    private var onGameOver: ((loser: BoardSide) -> Unit)? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (cell in pressedCells) {
            cell.drawClick(canvas)
        }
        currentPlayerBitmap?.let {
            currentPlayerImgRect?.let { rect ->
                canvas.drawBitmap(it, null, rect, null)
            }
        }
    }

    fun setLeftBoard(board: GameBoard) {
        leftBoard = board
    }

    fun setRightBoard(board: GameBoard) {
        rightBoard = board
        currentGameBoard = rightBoard
        currentShipsForCheck = rightShips
        initializeCurrentPlayerImg()
    }

    private fun initializeCurrentPlayerImg() {
        currentPlayerBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.right_plays)
        currentGameBoard?.setBoardLimits(context)
        val cellSize = currentGameBoard?.cellSize
        val rightForRect = (rightBoard?.leftLimit!! - cellSize!!).toInt()
        val topForRect = ((currentGameBoard?.bottomLimit!! / 2) - (currentGameBoard?.topLimit!! / 2)).toInt()
        val leftForRect = rightForRect.minus(cellSize)
        val bottomForRect = topForRect.plus(2 * cellSize)
        currentPlayerImgRect = Rect(leftForRect, topForRect, rightForRect, bottomForRect)
    }

    private fun setCurrentBoard() {
        if (currentGameBoard == rightBoard) {
            currentGameBoard = leftBoard
            currentShipsForCheck = leftShips
            currentPlayerBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.left_plays)
        } else {
            currentGameBoard = rightBoard
            currentShipsForCheck = rightShips
            currentPlayerBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.right_plays)
        }
    }

    private fun checkHits(imgRect: Rect): Boolean {
        for (ship in currentShipsForCheck!!) {
            if (ship.checkHits(imgRect)) {
                lastDamagedShip = ship
                return true
            }
        }
        return false
    }

    private fun getCellImageRect(
        x: Float,
        y: Float,
    ): Rect {
        return Rect(
            x.toInt(),
            y.toInt(),
            (x + currentGameBoard!!.cellSize).toInt(),
            (y + currentGameBoard!!.cellSize).toInt(),
        )
    }

    private fun addSurroundingCell(newCell: Cell) {
        val hasCellWithSameImgRect = pressedCells.any { it.imageRect.intersect(newCell.imageRect) }
        if (!hasCellWithSameImgRect) {
            pressedCells.add(newCell)
        }
    }

    private fun determineTheLoser() {
        currentGameBoard?.let { onGameOver?.invoke(it.side) }
    }

    fun setOnGameOver(listener: (winner: BoardSide) -> Unit) {
        onGameOver = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> handleActionDown(event.x, event.y)
        }
        return true
    }

    private fun handleActionDown(
        x: Float,
        y: Float,
    ) {
        if (currentGameBoard!!.checkPressureWithinBoard(x, y)) {
            val xCord = currentGameBoard!!.getXCellCordForShip(x)
            val yCord = currentGameBoard!!.getYCellCordForShip(y)
            val cellImageRect = getCellImageRect(xCord!!, yCord!!)
            processCellClick(cellImageRect)
        }
    }

    private fun processCellClick(cellImageRect: Rect) {
        val isCellPressed = pressedCells.any { it.imageRect.intersect(cellImageRect) }
        if (!isCellPressed) {
            val isHit = checkHits(cellImageRect)
            pressedCells.add(Cell(cellImageRect, isHit, context))
            if (isHit) {
                handleHit()
            } else {
                setCurrentBoard()
            }
            invalidate()
        }
    }

    private fun handleHit() {
        if (lastDamagedShip!!.checkDestruction()) {
            handleShipDestruction()
        }
    }

    private fun handleShipDestruction() {
        val surroundingCells = lastDamagedShip!!.getSurroundingCells()
        for (surroundingCellImageRect in surroundingCells) {
            if (currentGameBoard!!.checkImgRectWithinBoard(surroundingCellImageRect)) {
                addSurroundingCell(Cell(surroundingCellImageRect, false, context))
            }
        }
        currentShipsForCheck?.remove(lastDamagedShip!!)
        val shipsList = if (currentGameBoard == rightBoard) rightShips else leftShips
        shipsList?.remove(lastDamagedShip!!)
        if (rightShips?.isEmpty() == true || leftShips?.isEmpty() == true) {
            determineTheLoser()
        }
    }
}
