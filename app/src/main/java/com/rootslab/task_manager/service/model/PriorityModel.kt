package com.rootslab.task_manager.service.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "priority")
class PriorityModel {
    @PrimaryKey
    var id: Int = 0
    var description: String = ""
}
