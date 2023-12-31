package com.project.seaBattle

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class PlayerNameDialog : DialogFragment() {
    private var onNameEntered: ((name: String) -> Unit)? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.dialog_player_name_layout, container, false)

        val editTextForName: EditText = rootView.findViewById(R.id.enterName)

        val saveNameButton: Button = rootView.findViewById(R.id.saveName)
        saveNameButton.setOnClickListener {
            onNameEntered?.invoke(editTextForName.text.toString())
            dismiss()
        }

        return rootView
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
