package com.project.seaBattle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class GameBoard(val side: String) {
    var leftLimit: Float = 0.0f
    var topLimit: Float = 0.0f
    var cellSize: Int = 0
    var bottomLimit: Float = 0.0f
    var startXForPlacementShips: Int? = 0
    private var rightLimit: Float = 0.0f
    private var screenHeight: Float? = 0f
    private var lettersBitmap: Bitmap? = null
    private var numbersBitmap: Bitmap? = null
    private var lettersImgRect: Rect? = null
    private var numbersImgRect: Rect? = null
    private val verticalLines = mutableListOf<Float>()
    private val horizontalLines = mutableListOf<Float>()

    fun setBoardLimits(context: Context) {
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels.toFloat()
        val centerInWidth = screenWidth / 2
        screenHeight = displayMetrics.heightPixels.toFloat()
        val boardSize = calculateBoardSize(screenHeight!!)
        lettersBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.letters)
        numbersBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.numbers)
        setLimitsAndStartX(centerInWidth, boardSize, screenWidth)
        cellSize = calculateCellSize()
        setImgRectForImages()
        createLinesForBoard()
    }

    private fun calculateBoardSize(screenHeight: Float): Float {
        val indentFromTopPercentage = 0.1f
        return screenHeight * (1 - 2 * indentFromTopPercentage) - screenHeight * indentFromTopPercentage
    }

    private fun setLimitsAndStartX(
        centerInWidth: Float,
        boardSize: Float,
        screenWidth: Float,
    ) {
        val indentBoardFromCenterPercentage = 0.05f
        if (side == "Right") {
            leftLimit = centerInWidth + screenWidth * indentBoardFromCenterPercentage
            startXForPlacementShips = (centerInWidth - screenWidth * indentBoardFromCenterPercentage - boardSize).toInt()
        } else {
            leftLimit = centerInWidth - screenWidth * indentBoardFromCenterPercentage - boardSize
            startXForPlacementShips = (centerInWidth + screenWidth * indentBoardFromCenterPercentage).toInt()
        }
        topLimit = screenHeight!! / 2 - boardSize * 0.1f - boardSize / 2
        rightLimit = leftLimit + boardSize
        bottomLimit = topLimit + boardSize
    }

    private fun calculateCellSize(): Int {
        return ((rightLimit - leftLimit) / 10).toInt()
    }

    private fun setImgRectForImages() {
        val imgDisplacementCf = 1.2f
        val leftForLettersImgRect = (leftLimit - imgDisplacementCf * cellSize).toInt()
        val topForNumbersImgRect = (topLimit - imgDisplacementCf * cellSize).toInt()
        lettersImgRect = Rect(leftForLettersImgRect, topLimit.toInt(), leftForLettersImgRect + cellSize, bottomLimit.toInt())
        numbersImgRect = Rect(leftLimit.toInt(), topForNumbersImgRect, rightLimit.toInt(), topForNumbersImgRect + cellSize)
    }

    private fun createLinesForBoard() {
        val lineCountInOnePlane = 9
        val boardWidth = rightLimit - leftLimit
        val colsSize = boardWidth / 10
        var verticalLineStartX = leftLimit
        var horizontalLineStartY = topLimit
        for (i in 0..lineCountInOnePlane) {
            verticalLines.add(verticalLineStartX)
            horizontalLines.add(horizontalLineStartY)
            verticalLineStartX += colsSize
            horizontalLineStartY += colsSize
        }
        verticalLines.add(rightLimit)
        horizontalLines.add(bottomLimit)
    }

    fun checkPressureWithinBoard(
        x: Float,
        y: Float,
    ): Boolean {
        return (x in leftLimit..rightLimit && y in topLimit..bottomLimit)
    }

    fun checkImgRectWithinBoard(cellImgRect: Rect): Boolean {
        val boardRect = Rect(leftLimit.toInt(), topLimit.toInt(), rightLimit.toInt(), bottomLimit.toInt())
        return cellImgRect.intersect((boardRect))
    }

    fun drawBoard(
        canvas: Canvas,
        paint: Paint,
    ) {
        for (line in verticalLines) canvas.drawLine(line, topLimit, line, bottomLimit, paint)
        for (line in horizontalLines) canvas.drawLine(leftLimit, line, rightLimit, line, paint)
        lettersBitmap?.let {
            lettersImgRect?.let { rect ->
                canvas.drawBitmap(it, null, rect, null)
            }
        }
        numbersBitmap?.let {
            numbersImgRect?.let { rect ->
                canvas.drawBitmap(it, null, rect, null)
            }
        }
    }

    fun getXCellCordForShip(x: Float): Float? {
        val rightVerticalLineOfCell = verticalLines.find { it > x }
        return rightVerticalLineOfCell?.minus(cellSize)
    }

    fun getYCellCordForShip(y: Float): Float? {
        val lowerHorizontalLineOfCell = horizontalLines.find { it > y }
        return lowerHorizontalLineOfCell?.minus(cellSize)
    }

    fun getShipDisplacement(
        x: Float,
        y: Float,
        shipLength: Int,
        shipPosition: String,
    ): Float {
        val permissibleLine: Float
        var shipDisplacement = 0f
        val linesForCheck = if (shipPosition == "Horizontal") verticalLines else horizontalLines
        val numberPermissibleLine = linesForCheck.size - shipLength
        permissibleLine = linesForCheck[numberPermissibleLine]
        val cellCord = if (shipPosition == "Horizontal") getXCellCordForShip(x) else getYCellCordForShip(y)
        if (cellCord!! - permissibleLine > 0) shipDisplacement = cellCord - permissibleLine + cellSize
        return shipDisplacement
    }
}
