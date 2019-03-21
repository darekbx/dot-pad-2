package com.dotpad2.repository

import android.content.res.Resources
import com.dotpad2.R

class ColorMapper(private val resources: Resources) {

    private val colorMap = mapOf(
        resources.getColor(R.color.legacyDotRed) to resources.getColor(R.color.dotRed),
        resources.getColor(R.color.legacyDotGreen) to resources.getColor(R.color.dotTeal),
        resources.getColor(R.color.legacyDotBlue) to resources.getColor(R.color.dotBlue),
        resources.getColor(R.color.legacyDotPurple) to resources.getColor(R.color.legacyDotPurple),
        resources.getColor(R.color.legacyDotViolet) to resources.getColor(R.color.legacyDotViolet),
        resources.getColor(R.color.legacyDotYellow) to resources.getColor(R.color.dotYellow)
    )

    fun mapLegacyColor(legacyColor: Int) = colorMap[legacyColor]
}