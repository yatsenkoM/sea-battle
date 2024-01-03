package com.project.seaBattle

import android.annotation.SuppressLint
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

class BattleActivity : AppCompatActivity() {
    private val leftGameBoard = GameBoard("Left")
    private val rightGameBoard = GameBoard("Right")

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)
        val drawingBoard = findViewById<DrawingBoard>(R.id.drawingBoard)
        val battleEditor = findViewById<BattleEditor>(R.id.battleEditor)
        val firstPlayerName = findViewById<TextView>(R.id.firstPlayerName)
        val secondPlayerName = findViewById<TextView>(R.id.secondPlayerName)
        drawingBoard.setBoard(rightGameBoard)
        drawingBoard.setBoard(leftGameBoard)
        val gameInfoManager = GameInfoManager.getInstance()
        firstPlayerName.text = gameInfoManager.leftPlayerName
        secondPlayerName.text = gameInfoManager.rightPlayerName
        battleEditor.leftShips = gameInfoManager.leftShips
        battleEditor.rightShips = gameInfoManager.rightShips
        battleEditor.setRightBoard(rightGameBoard)
        battleEditor.setLeftBoard(leftGameBoard)
        battleEditor.setOnGameOver { loser ->
            gameInfoManager.winnerName = if (loser == "Right") gameInfoManager.leftPlayerName else gameInfoManager.rightPlayerName
            gameInfoManager.winnerName?.let { showWinnerDialog(it) }
            gameInfoManager.resetAllInfo()
        }
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
