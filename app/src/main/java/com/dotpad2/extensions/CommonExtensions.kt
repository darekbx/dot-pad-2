package com.dotpad2.extensions

import com.dotpad2.ui.dotdialog.colorselect.ColorSelectorView
import com.dotpad2.ui.dotdialog.sizeselect.SizeSelectorView

fun <T> ColorSelectorView.childLoop(callback: (Int, T) -> Unit) {
    for (index in 0 until getAdapter().count) {
        val view = getChildAt(index) as T
        callback(index, view)
    }
}


fun <T> SizeSelectorView.childLoop(callback: (Int, T) -> Unit) {
    for (index in 0 until adapter.count) {
        val view = getChildAt(index) as T
        callback(index, view)
    }
}