package com.project.seaBattle.dialogs

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.project.seaBattle.activities.StartActivity
import com.project.seaBattle.databinding.DialogWinnerLayoutBinding

class ShowingWinnerDialog(private val winnerName: String) : DialogFragment() {
    @Suppress("ktlint:standard:property-naming")
    private var _binding: DialogWinnerLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogWinnerLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.winner.text = winnerName
        binding.returnStartActivity.setOnClickListener {
            val intent = Intent(context, StartActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}
