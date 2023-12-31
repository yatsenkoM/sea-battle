package com.project.seaBattle

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class SetRightShipsActivity: AppCompatActivity() {
    private val gameBoard = GameBoard("Right")

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_right_ships)
        val playerNameDialog = PlayerNameDialog()
        playerNameDialog.show(supportFragmentManager, "PlayerNameDialog")
        val gameInfoManager = GameInfoManager.getInstance()
        playerNameDialog.setOnNameEntered { name ->
            gameInfoManager.rightPlayerName = name
        }
        val drawingBoard = findViewById<DrawingBoard>(R.id.drawingBoard)
        val shipsSetter = findViewById<ShipsSetter>(R.id.shipsSetter)
        val shipsInstalledButton = findViewById<Button>(R.id.shipsInstalled)
        drawingBoard.setRightBoard(gameBoard)
        shipsSetter.gameBoard = gameBoard
        shipsSetter.initializeShips()
        shipsInstalledButton.setOnClickListener {
            if(shipsSetter.areAllShipsInstalled()) {
                gameInfoManager.addRightShips(shipsSetter.ships)
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
