package com.project.seaBattle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startBattleButton = findViewById<ImageButton>(R.id.startButton)
        val shipsForFirstPlayerButton = findViewById<Button>(R.id.player1Button)
        val shipsForSecondPlayerButton = findViewById<Button>(R.id.player2Button)
        val resetInfoButton = findViewById<Button>(R.id.resetInfo)
        val gameInfoManager = GameInfoManager.getInstance()
        shipsForFirstPlayerButton.setOnClickListener {
            if (gameInfoManager.leftShips.isNotEmpty()) {
                showToast("Кораблі для першого гравця вже встановлені")
            } else {
                val intent = Intent(this, SetShipsActivity::class.java)
                intent.putExtra("SIDE", BoardSide.LEFT.name)
                startActivity(intent)
            }
        }
        shipsForSecondPlayerButton.setOnClickListener {
            if (gameInfoManager.rightShips.isNotEmpty()) {
                showToast("Кораблі для другого гравця вже встановлені")
            } else {
                val intent = Intent(this, SetShipsActivity::class.java)
                intent.putExtra("SIDE", BoardSide.RIGHT.name)
                startActivity(intent)
            }
        }
        startBattleButton.setOnClickListener {
            if (gameInfoManager.leftShips.isNotEmpty() && gameInfoManager.rightShips.isNotEmpty()) {
                val intent = Intent(this, BattleActivity::class.java)
                startActivity(intent)
            } else {
                showToast("Кораблі встановлені не для всіх гравців")
            }
        }
        resetInfoButton.setOnClickListener {
            gameInfoManager.resetAllInfo()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
