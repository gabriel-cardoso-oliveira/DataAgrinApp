package com.dataagrin.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataagrin.project.model.ActivityLog
import com.dataagrin.project.repository.ActivityRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActivityViewModel(private val repository: ActivityRepository) : ViewModel() {
    val activities = repository.activities.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addActivity(activity: ActivityLog) {
        viewModelScope.launch {
            repository.addActivity(activity)
        }
    }
}