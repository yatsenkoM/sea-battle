package com.project.seaBattle.logics.shipsSetting

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.project.seaBattle.R
import com.project.seaBattle.logics.board.GameBoard
import com.project.seaBattle.logics.ship.Ship
import com.project.seaBattle.logics.ship.ShipOrientation

class ShipsSetter(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    var gameBoard: GameBoard? = null
    val ships = mutableListOf<Ship>()
    private var movingShip: Ship? = null
    private var touchX = 0f
    private var touchY = 0f
    private var cellSize: Int = 0
    private var shipCapsizes: Boolean = false
    private var xUntilMoving = 0
    private var yUntilMoving = 0

    fun initializeShips() {
        gameBoard?.setBoardLimits(context)
        cellSize = gameBoard?.cellSize!!
        addShips(R.drawable.ship_single, 4, cellSize)
        addShips(R.drawable.ship_double, 3, cellSize)
        addShips(R.drawable.ship_triple, 2, cellSize)
        addShips(R.drawable.ship_fourfold, 1, cellSize)
    }

    private fun addShips(
        resourceId: Int,
        count: Int,
        cellSize: Int,
    ) {
        val yIndentFromBoardTop = 50f
        val startY = (gameBoard?.topLimit!! + yIndentFromBoardTop).toInt()
        val startX = gameBoard?.startXForPlacementShips
        var distanceMultiplier = 0
        repeat(count) {
            val newShip = Ship(context, resourceId, cellSize)
            val shipTypesNumber = 4
            newShip.currentY = startY + ((shipTypesNumber - count) * cellSize * 2)
            val xDistanceBetweenShips = 7 - count
            if (startX != null) newShip.currentX = startX + distanceMultiplier * cellSize * xDistanceBetweenShips
            newShip.startX = newShip.currentX
            newShip.startY = newShip.currentY
            newShip.setStartPosition()
            ships.add(newShip)
            distanceMultiplier++
        }
    }

    private fun checkPossibilityInstallingShip(): Boolean {
        var falseCount = 0
        for (ship in ships) {
            movingShip?.imageRect?.let {
                if (ship.checkNeighborhoods(it)) {
                    falseCount++
                    if (falseCount == 2) return false
                }
            }
        }
        return true
    }

    fun areAllShipsInstalled(): Boolean {
        for (ship in ships) {
            if (ship.imageRect?.let { gameBoard?.checkImgRectWithinBoard(it) } != true) {
                return false
            }
        }
        return true
    }

    private fun handleShipMovement(
        ship: Ship,
        touchX: Float,
        touchY: Float,
    ) {
        val displacement = gameBoard?.getShipDisplacement(touchX, touchY, ship.shipLength, ship.shipPosition)
        ship.currentX = gameBoard?.getXCellCordForShip(touchX)?.toInt()!!
        ship.currentY = gameBoard?.getYCellCordForShip(touchY)?.toInt()!!
        if (ship.shipPosition == ShipOrientation.HORIZONTAL) {
            ship.currentX -= displacement!!.toInt()
        } else {
            ship.currentY -= displacement!!.toInt()
        }
    }

    private fun updateImageRect(ship: Ship) {
        val width = if (ship.shipPosition == ShipOrientation.HORIZONTAL) cellSize * ship.shipLength else cellSize
        val height = if (ship.shipPosition == ShipOrientation.HORIZONTAL) cellSize else cellSize * ship.shipLength
        ship.imageRect?.set(ship.currentX, ship.currentY, ship.currentX + width, ship.currentY + height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (ship in ships) {
            ship.draw(canvas)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchX = event.x
        touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                shipCapsizes = true
                for (ship in ships) {
                    if (ship.imageRect!!.contains(touchX.toInt(), touchY.toInt())) {
                        movingShip = ship
                        movingShip?.let {
                            xUntilMoving = it.currentX
                            yUntilMoving = it.currentY
                            it.currentX = touchX.toInt()
                            it.currentY = touchY.toInt()
                        }
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                shipCapsizes = false
                movingShip?.let {
                    val deltaX = touchX.toInt() - it.currentX
                    val deltaY = touchY.toInt() - it.currentY
                    val width = if (it.shipPosition == ShipOrientation.HORIZONTAL) cellSize * it.shipLength else cellSize
                    val height = if (it.shipPosition == ShipOrientation.HORIZONTAL) cellSize else cellSize * it.shipLength
                    it.currentX += deltaX
                    it.currentY += deltaY
                    it.imageRect!!.set(
                        it.currentX,
                        it.currentY,
                        it.currentX + width,
                        it.currentY + height,
                    )
                }
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                if (gameBoard!!.checkPressureWithinBoard(touchX, touchY)) {
                    movingShip?.let {
                        if (!checkPossibilityInstallingShip()) {
                            it.moveShipTo(xUntilMoving, yUntilMoving)
                        } else {
                            if (gameBoard?.checkPressureWithinBoard(touchX, touchY) == true) {
                                handleShipMovement(it, touchX, touchY)
                                if (shipCapsizes) {
                                    it.toggleShipOrientation()
                                    handleShipMovement(it, touchX, touchY)
                                }
                                updateImageRect(it)
                                if (!checkPossibilityInstallingShip()) {
                                    if (shipCapsizes) it.toggleShipOrientation()
                                    it.moveShipTo(xUntilMoving, yUntilMoving)
                                }
                            }
                        }
                        updateImageRect(it)
                    }
                } else {
                    movingShip?.setStartPosition()
                }
                invalidate()
            }
        }
        return true
    }
}
