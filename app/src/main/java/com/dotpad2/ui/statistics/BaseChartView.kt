package com.dotpad2.ui.statistics

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.dotpad2.model.StatisticsValue

open class BaseChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var data: List<Pair<Float, StatisticsValue>>? = null
}