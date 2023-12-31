package com.project.seaBattle

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class SetLeftShipsActivity : AppCompatActivity() {
    private val gameBoard = GameBoard("Left")

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_left_ships)
        val playerNameDialog = PlayerNameDialog()
        playerNameDialog.show(supportFragmentManager, "PlayerNameDialog")
        val gameInfoManager = GameInfoManager.getInstance()
        playerNameDialog.setOnNameEntered { name ->
            gameInfoManager.leftPlayerName = name
        }
        val drawingBoard = findViewById<DrawingBoard>(R.id.drawingBoard)
        val shipsSetter = findViewById<ShipsSetter>(R.id.shipsSetter)
        val shipsInstalledButton = findViewById<Button>(R.id.shipsInstalled)
        drawingBoard.setLeftBoard(gameBoard)
        shipsSetter.gameBoard = gameBoard
        shipsSetter.initializeShips()
        shipsInstalledButton.setOnClickListener {
            if(shipsSetter.areAllShipsInstalled()) {
                gameInfoManager.addLeftShips(shipsSetter.ships)
                finish()
            }
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                shipsSetter.areAllShipsInstalled()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

}