package com.dotpad2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dotpad2.model.StatisticsValue
import com.dotpad2.repository.Repository
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun sizes() = mapToPercents(repository.fetchSizeStatistics())
    fun colors() = mapToPercents(repository.fetchColorStatistics())

    private fun mapToPercents(values: LiveData<List<StatisticsValue>>) =
        Transformations.map(values, { statisticValues ->
            val sum = statisticValues.sumBy { it.occurrences ?: 0 }.toFloat()
            return@map statisticValues.map { statisticValue ->
                val percent = (statisticValue.occurrences ?: 0) / sum * 100F
                return@map Pair(percent, statisticValue)
            }
        })
}