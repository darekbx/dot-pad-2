package com.dotpad2.repository

import android.content.res.Resources
import com.dotpad2.R

class ColorMapper(private val resources: Resources) {

    val colorMap = mapOf(
        parseColor(R.color.legacyDotRed) to parseColor(R.color.dotRed),
        parseColor(R.color.legacyDotGreen) to parseColor(R.color.dotTeal),
        parseColor(R.color.legacyDotBlue) to parseColor(R.color.dotBlue),
        parseColor(R.color.legacyDotViolet) to parseColor(R.color.dotPurple),
        parseColor(R.color.legacyDotPurple) to parseColor(R.color.dotOrange),
        parseColor(R.color.legacyDotYellow) to parseColor(R.color.dotYellow)
    )

    fun mapLegacyColor(legacyColor: Int) = colorMap[legacyColor] ?: legacyColor

    private fun parseColor(colorResId: Int) = resources.getColor(colorResId, null)
}