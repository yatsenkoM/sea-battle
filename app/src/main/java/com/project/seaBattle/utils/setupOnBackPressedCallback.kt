package com.project.seaBattle.utils

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback

fun OnBackPressedDispatcherOwner.setupOnBackPressedCallback(onBackPressedAction: () -> Unit) {
    onBackPressedDispatcher.addCallback(this) {
        onBackPressedAction.invoke()
    }
}
