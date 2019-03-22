package com.dotpad2.repository

import android.util.Log
import androidx.lifecycle.Transformations
import com.dotpad2.model.Dot
import com.dotpad2.repository.local.DotsDao
import com.dotpad2.repository.local.LegacyDotsDao
import com.dotpad2.repository.local.entities.DotDto

class Repository(
    private val dotsDao: DotsDao,
    private val legacyDotsDao: LegacyDotsDao,
    private val colorMapper: ColorMapper) {

    fun fetchActive() =
        Transformations.map(dotsDao.fetchActive(), { mapDotDtosToDots(it) })

    fun fetchAll(limit: Int, offset: Int) =
        Transformations.map(dotsDao.fetchAll(limit, offset), { mapDotDtosToDots(it) })

    fun getDot(dotId: Long) =
        Transformations.map(dotsDao.fetchDot(dotId), { Mappers.dtoDtoToDot(it) })

    fun add(dot: Dot) {
        val dotDto = Mappers.dtoToDotDto(dot)
        dotsDao.add(dotDto)
    }

    fun updatePosition(dot: Dot) {
        dotsDao.updateDotPosition(dot.id, dot.position.x, dot.position.y)
    }

    fun update(dot: Dot) {
        val dotDto = Mappers.dtoToDotDto(dot)
        dotsDao.update(dotDto)
    }

    fun fetchSizeStatistics() = dotsDao.sizeStatistics()

    fun fetchColorStatistics() = dotsDao.colorStatistics()

    fun fetchCountStatistics() = dotsDao.countStatistics()

    fun addAllDtos(dotDtos: List<DotDto>) {
        dotsDao.addAll(dotDtos)
    }

    fun updateLegacyColors() {
        colorMapper.colorMap.keys.forEach { legacyColor ->
            val updatedCount = dotsDao.updateColor(colorMapper.colorMap[legacyColor] ?: legacyColor, legacyColor)
        }
    }

    private fun mapDotDtosToDots(dotDtos: List<DotDto>) =
        dotDtos.map { dotDto -> Mappers.dtoDtoToDot(dotDto) }
}