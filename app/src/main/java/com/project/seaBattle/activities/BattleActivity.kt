package com.project.seaBattle.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.seaBattle.logics.shipsSetting.GameInfoManager
import com.project.seaBattle.logics.board.BoardSide
import com.project.seaBattle.logics.board.GameBoard
import com.project.seaBattle.databinding.ActivityBattleBinding
import com.project.seaBattle.dialogs.ShowingWinnerDialog
import com.project.seaBattle.utils.setupOnBackPressedCallback
import com.project.seaBattle.utils.showToast

class BattleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBattleBinding
    private val leftGameBoard = GameBoard(BoardSide.LEFT)
    private val rightGameBoard = GameBoard(BoardSide.RIGHT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBattleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupGame()
        setupOnBackPressedCallback {
            showToast("Ця кнопка не працює в грі", this)
        }
    }

    private fun setupGame() {
        val gameInfoManager = GameInfoManager.getInstance()
        binding.drawingBoard.setBoard(rightGameBoard)
        binding.drawingBoard.setBoard(leftGameBoard)
        binding.firstPlayerName.text = gameInfoManager.leftPlayerName
        binding.secondPlayerName.text = gameInfoManager.rightPlayerName
        binding.battleEditor.leftShips = gameInfoManager.leftShips
        binding.battleEditor.rightShips = gameInfoManager.rightShips
        binding.battleEditor.setRightBoard(rightGameBoard)
        binding.battleEditor.setLeftBoard(leftGameBoard)
        binding.battleEditor.setOnGameOver { loser ->
            gameInfoManager.winnerName = if (loser == BoardSide.RIGHT) gameInfoManager.leftPlayerName else gameInfoManager.rightPlayerName
            gameInfoManager.winnerName?.let {
                val showingWinnerDialog = ShowingWinnerDialog(it)
                showingWinnerDialog.show(supportFragmentManager, "ShowingWinnerDialog")
            }
            gameInfoManager.resetAllInfo()
        }
    }
}
