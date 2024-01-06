package com.project.seaBattle.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.seaBattle.databinding.ActivitySetShipsBinding
import com.project.seaBattle.dialogs.PlayerNameDialog
import com.project.seaBattle.logics.board.BoardSide
import com.project.seaBattle.logics.board.GameBoard
import com.project.seaBattle.logics.shipsSetting.GameInfoManager
import com.project.seaBattle.utils.setupOnBackPressedCallback
import com.project.seaBattle.utils.showToast

class SetShipsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetShipsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetShipsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val boardSideName = intent.getStringExtra("SIDE")
        val boardSide = boardSideName?.let { BoardSide.valueOf(it) }
        val gameBoard = boardSide?.let { GameBoard(it) }
        val gameInfoManager = GameInfoManager.getInstance()
        val playerNameDialog = PlayerNameDialog()
        playerNameDialog.show(supportFragmentManager, "PlayerNameDialog")
        playerNameDialog.setOnNameEntered { name ->
            gameInfoManager.setPlayerName(name, boardSide!!)
        }
        binding.drawingBoard.setBoard(gameBoard!!)
        binding.shipsSetter.gameBoard = gameBoard
        binding.shipsSetter.initializeShips()
        binding.shipsInstalled.setOnClickListener {
            if (binding.shipsSetter.areAllShipsInstalled()) {
                gameInfoManager.addShips(binding.shipsSetter.ships, boardSide)
                finish()
            } else {
                showToast("Not all ships are installed", this)
            }
        }
        setupOnBackPressedCallback {
            showToast("This button does not work in the game", this)
        }
    }
}
