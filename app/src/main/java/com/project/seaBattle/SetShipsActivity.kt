package com.project.seaBattle

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class SetShipsActivity : AppCompatActivity() {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_ships)
        val boardSideName = intent.getStringExtra("SIDE")
        val boardSide = boardSideName?.let { BoardSide.valueOf(it) }
        val gameBoard = boardSide?.let { GameBoard(it) }
        val gameInfoManager = GameInfoManager.getInstance()
        val playerNameDialog = PlayerNameDialog()
        val drawingBoard = findViewById<DrawingBoard>(R.id.drawingBoard)
        val shipsSetter = findViewById<ShipsSetter>(R.id.shipsSetter)
        val shipsInstalledButton = findViewById<Button>(R.id.shipsInstalled)
        playerNameDialog.show(supportFragmentManager, "PlayerNameDialog")
        playerNameDialog.setOnNameEntered { name ->
            gameInfoManager.setPlayerName(name, boardSide!!)
        }
        drawingBoard.setBoard(gameBoard!!)
        shipsSetter.gameBoard = gameBoard
        shipsSetter.initializeShips()
        shipsInstalledButton.setOnClickListener {
            if (shipsSetter.areAllShipsInstalled()) {
                gameInfoManager.addShips(shipsSetter.ships, boardSide)
                finish()
            }
        }
        val callback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    shipsSetter.areAllShipsInstalled()
                }
            }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}
