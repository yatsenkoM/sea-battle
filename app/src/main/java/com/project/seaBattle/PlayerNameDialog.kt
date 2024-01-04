package com.project.seaBattle

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.project.seaBattle.databinding.DialogPlayerNameLayoutBinding

class PlayerNameDialog : DialogFragment() {
    @Suppress("ktlint:standard:property-naming")
    private var _binding: DialogPlayerNameLayoutBinding? = null
    private val binding get() = _binding!!
    private var onNameEntered: ((name: String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogPlayerNameLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.saveName.setOnClickListener {
            onNameEntered?.invoke(binding.enterName.text.toString())
            dismiss()
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

    fun setOnNameEntered(listener: (name: String) -> Unit) {
        onNameEntered = listener
    }
}
