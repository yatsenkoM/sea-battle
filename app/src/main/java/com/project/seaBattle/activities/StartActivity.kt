package com.project.seaBattle.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.seaBattle.databinding.ActivityMainBinding
import com.project.seaBattle.logics.board.BoardSide
import com.project.seaBattle.logics.shipsSetting.GameInfoManager
import com.project.seaBattle.utils.showToast

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val gameInfoManager = GameInfoManager.getInstance()
        binding.player1Button.setOnClickListener {
            if (gameInfoManager.leftShips.isNotEmpty()) {
                showToast("The ships for the first player are already installed", this)
            } else {
                val intent = Intent(this, SetShipsActivity::class.java)
                intent.putExtra("SIDE", BoardSide.LEFT.name)
                startActivity(intent)
            }
        }
        binding.player2Button.setOnClickListener {
            if (gameInfoManager.rightShips.isNotEmpty()) {
                showToast("The ships for the second player are already installed", this)
            } else {
                val intent = Intent(this, SetShipsActivity::class.java)
                intent.putExtra("SIDE", BoardSide.RIGHT.name)
                startActivity(intent)
            }
        }
        binding.startButton.setOnClickListener {
            if (gameInfoManager.leftShips.isNotEmpty() && gameInfoManager.rightShips.isNotEmpty()) {
                val intent = Intent(this, BattleActivity::class.java)
                startActivity(intent)
            } else {
                showToast("Ships are not installed for all players", this)
            }
        }
        binding.resetInfo.setOnClickListener {
            gameInfoManager.resetAllInfo()
        }
    }
}
