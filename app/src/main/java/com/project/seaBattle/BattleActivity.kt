package com.project.seaBattle

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.project.seaBattle.databinding.ActivityBattleBinding

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
        setupOnBackPressedCallback()
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
            gameInfoManager.winnerName?.let { showWinnerDialog(it) }
            gameInfoManager.resetAllInfo()
        }
    }

    private fun setupOnBackPressedCallback() {
        val callback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun showWinnerDialog(winnerName: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_winner_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val winner: TextView = dialog.findViewById(R.id.winner)
        winner.text = winnerName
        val returnStartActivityButton: Button = dialog.findViewById(R.id.returnStartActivity)
        returnStartActivityButton.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
    }
}
