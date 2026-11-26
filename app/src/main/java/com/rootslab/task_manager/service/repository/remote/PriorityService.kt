package com.rootslab.task_manager.service.repository.remote

import com.rootslab.task_manager.service.model.PriorityModel
import retrofit2.Response
import retrofit2.http.GET

interface PriorityService {

    @GET("Priority")
    suspend fun list(): Response<List<PriorityModel>>

}
