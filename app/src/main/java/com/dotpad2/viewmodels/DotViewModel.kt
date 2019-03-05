package com.dotpad2.viewmodels

import androidx.lifecycle.ViewModel
import com.dotpad2.model.Dot
import com.dotpad2.repository.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject

class DotViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun allDots(limit: Int, offset: Int) = repository.fetchAll(limit, offset)

    fun activeDots() = repository.fetchActive()

    fun loadDot(dotId: Long) = repository.getDot(dotId)

    suspend fun saveDot(dot: Dot) {
        GlobalScope.async {
            when (dot.id) {
                null -> repository.add(dot)
                else -> repository.update(dot)
            }
        }.await()
    }
}