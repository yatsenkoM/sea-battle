package com.project.seaBattle

class GameInfoManager private constructor() {
    val rightShips: MutableList<Ship> = mutableListOf()
    val leftShips: MutableList<Ship> = mutableListOf()
    var leftPlayerName: String? = null
    var rightPlayerName: String? = null
    var winnerName: String? = null

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
