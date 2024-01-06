package com.project.seaBattle.logics.shipsSetting

import com.project.seaBattle.logics.board.BoardSide
import com.project.seaBattle.logics.ship.Ship

class GameInfoManager private constructor() {
    var rightPlayerName: String? = null
    var winnerName: String? = null
    val rightShips: MutableList<Ship> = mutableListOf()
    val leftShips: MutableList<Ship> = mutableListOf()
    var leftPlayerName: String? = null

    fun addShips(
        ships: MutableList<Ship>,
        side: BoardSide,
    ) {
        if (side == BoardSide.RIGHT) {
            rightShips.addAll(ships)
        } else {
            leftShips.addAll(ships)
        }
    }

    fun setPlayerName(
        name: String,
        side: BoardSide,
    ) {
        if (side == BoardSide.RIGHT) {
            rightPlayerName = name
        } else {
            leftPlayerName = name
        }
    }

    fun resetAllInfo() {
        rightShips.clear()
        leftShips.clear()
        leftPlayerName = null
        rightPlayerName = null
        winnerName = null
    }

    companion object {
        private var instance: GameInfoManager? = null

        fun getInstance(): GameInfoManager {
            if (instance == null) {
                instance = GameInfoManager()
            }
            return instance!!
        }
    }
}
