package com.project.seaBattle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect

class Ship(private val context: Context, resourceId: Int, private val cellSize: Int) {
    private var bitmap: Bitmap? = null
    var currentX: Int = 0
    var currentY: Int = 0
    var startX: Int = 0
    var startY: Int = 0
    var imageRect: Rect? = null
    private var constantForShipSize: Float = 0.0f
    var shipLength: Int = 0
    var shipPosition = "Horizontal"
    private var remainingCells: Int = 0

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
        setConstants()
        setStartPosition()
    }

    private fun setConstants() {
        val smallestShip = BitmapFactory.decodeResource(context.resources, R.drawable.ship_single)
        constantForShipSize = smallestShip.width.toFloat()
        shipLength = (bitmap!!.width / constantForShipSize).toInt()
        remainingCells = shipLength
    }

    fun draw(canvas: Canvas) {
        bitmap?.let {
            imageRect?.let { rect ->
                canvas.drawBitmap(it, null, rect, null)
            }
        }
    }

    fun setStartPosition() {
        if(shipPosition == "Vertical") setShipHorizontal()
        val width = cellSize * shipLength
        imageRect = Rect(
            startX,
            startY,
            startX + width,
            startY + cellSize
        )
    }

    private fun setShipVertical() {
        shipPosition = "Vertical"
        bitmap?.let {
            val matrix = Matrix()
            matrix.postRotate(90f)
            bitmap = Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
        }
    }

    private fun setShipHorizontal() {
        shipPosition = "Horizontal"
        bitmap?.let {
            val matrix = Matrix()
            matrix.postRotate(-90f)
            bitmap = Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
        }
    }

    fun toggleShipOrientation() {
        if(shipPosition == "Horizontal")
            setShipVertical()
        else
            setShipHorizontal()
    }

    private fun getUnavailableSpace(): Rect {
        return Rect(
            imageRect!!.left - cellSize/2,
            imageRect!!.top - cellSize/2,
            imageRect!!.right + cellSize,
            imageRect!!.bottom + cellSize
        )
    }

    fun checkNeighborhoods(shipSpace: Rect): Boolean {
        return shipSpace.intersect(getUnavailableSpace())
    }

    fun checkHits(cellImgRect: Rect): Boolean {
        val isHit = cellImgRect.intersect(imageRect!!)
        if(isHit) remainingCells--
        return isHit
    }

    fun checkDestruction() : Boolean {
        return remainingCells == 0
    }

    fun moveShipTo(x: Int, y: Int) {
        currentX = x
        currentY = y
    }

    private fun calculateOutermostCells(): Pair<Rect, Rect> {
        val width = shipLength * cellSize
        return if (shipPosition == "Horizontal") {
            Pair(
                Rect(currentX - cellSize, currentY, currentX, currentY + cellSize),
                Rect(currentX + width, currentY, currentX + width + cellSize, currentY + cellSize)
            )
        } else {
            Pair(
                Rect(currentX, currentY - cellSize, currentX + cellSize, currentY),
                Rect(currentX, currentY + width, currentX + cellSize, currentY + width + cellSize)
            )
        }
    }

    private fun addCellsToList(
        cellsList: MutableList<Rect>,
        firstOutermostCell: Rect,
        secondOutermostCell: Rect
    ) {
        cellsList.add(firstOutermostCell)
        cellsList.add(secondOutermostCell)
    }

    private fun addSurroundingCells(cellsList: MutableList<Rect>, outermostCell: Rect, isHorizontal: Boolean) {
        val left = outermostCell.left
        val right = outermostCell.right
        val top = outermostCell.top
        val bottom = outermostCell.bottom
        for (cell in 0 until shipLength + 2) {
            val leftOffset = left + cell * cellSize
            val rightOffset = right + cell * cellSize
            val topOffset = top + cell * cellSize
            val bottomOffset = bottom + cell * cellSize
            if (isHorizontal) {
                cellsList.add(Rect(leftOffset, currentY - cellSize, rightOffset, currentY))
                cellsList.add(Rect(leftOffset, currentY + cellSize, rightOffset, currentY + 2 * cellSize))
            } else {
                cellsList.add(Rect(currentX - cellSize, topOffset, currentX, bottomOffset))
                cellsList.add(Rect(currentX + cellSize, topOffset, currentX + 2 * cellSize, bottomOffset))
            }
        }
    }

    fun getSurroundingCells(): MutableList<Rect> {
        val surroundingCells = mutableListOf<Rect>()
        val (firstOutermostCell, secondOutermostCell) = calculateOutermostCells()
        addCellsToList(surroundingCells, firstOutermostCell, secondOutermostCell)
        addSurroundingCells(surroundingCells, firstOutermostCell, shipPosition == "Horizontal")
        return surroundingCells
    }
}