package com.dotpad2.viewmodels

import androidx.lifecycle.ViewModel
import com.dotpad2.model.Dot
import com.dotpad2.repository.Repository
import com.dotpad2.repository.local.entities.DotDto
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DotViewModel @Inject constructor(private val repository: Repository) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun allDots(limit: Int, offset: Int) = repository.fetchAll(limit, offset)

    fun activeDots() = repository.fetchActive()

    fun loadDot(dotId: Long) = repository.getDot(dotId)

    suspend fun addAllDtos(dotDtos: List<DotDto>) {
        GlobalScope.async {
            repository.addAllDtos(dotDtos)
        }.await()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    suspend fun saveDotPosition(dot: Dot) {
        GlobalScope.async {
            repository.updatePosition(dot)
        }.await()
    }

    suspend fun saveDot(dot: Dot) {
        GlobalScope.async {
            when (dot.id) {
                null -> repository.add(dot)
                else -> repository.update(dot)
            }
        }.await()
    }
}