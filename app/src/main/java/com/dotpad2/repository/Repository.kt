package com.dotpad2.repository

import androidx.lifecycle.Transformations
import com.dotpad2.model.Dot
import com.dotpad2.repository.local.DotsDao
import com.dotpad2.repository.local.entities.DotDto

class Repository(private val dotsDao: DotsDao) {

    fun fetchActive() =
        Transformations.map(dotsDao.fetchActive(), { mapDotDtosToDots(it) })

    fun fetchAll(limit: Int, offset: Int) =
        Transformations.map(dotsDao.fetchAll(limit, offset), { mapDotDtosToDots(it) })

    fun add(dot: Dot) {

    }

    fun update(dot: Dot) {

    }

    fun markDotArchived(dotId: Long, isArchived: Boolean) {
        dotsDao.markDotArchived(dotId, isArchived)
    }

    private fun mapDotDtosToDots(dotDtos: List<DotDto>) =
        dotDtos.map { dotDto -> Mappers.dtoDtoToDot(dotDto) }
}