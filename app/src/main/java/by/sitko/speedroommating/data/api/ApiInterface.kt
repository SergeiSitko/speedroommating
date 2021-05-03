package com.quiz.users.api

import by.sitko.speedroommating.data.EventItem
import retrofit2.http.GET
import java.io.IOException

interface ApiInterface {

    @GET("b/605479c67ffeba41c07de021")
    @Throws(IOException::class)
    suspend fun getEvents(): List<EventItem>
}