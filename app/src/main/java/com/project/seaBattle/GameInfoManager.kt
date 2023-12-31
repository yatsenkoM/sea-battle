package com.project.seaBattle

class GameInfoManager private constructor() {
    val rightShips: MutableList<Ship> = mutableListOf()
    val leftShips: MutableList<Ship> = mutableListOf()
    var leftPlayerName: String? = null
    var rightPlayerName: String? = null
    var winnerName: String? = null

    fun addRightShips(ships: MutableList<Ship>) {
        rightShips.addAll(ships)
    }

    fun addLeftShips(ships: MutableList<Ship>) {
        leftShips.addAll(ships)
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
