package com.example.vaccinechecker.data.api

import com.example.vaccinechecker.data.model.district.Districts
import com.example.vaccinechecker.data.model.locations.VaccineLocationRes
import com.example.vaccinechecker.data.model.states.States
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface BackendApis {

    @GET("api/v2/appointment/sessions/public/calendarByPin")
    suspend fun getDataByPincode(@QueryMap params: Map<String, String>): Response<VaccineLocationRes>

    @GET("api/v2/appointment/sessions/public/calendarByDistrict")
    suspend fun getDataByDistrict(@QueryMap params: Map<String, String>): Response<VaccineLocationRes>

    @GET("api/v2/admin/location/states")
    suspend fun getStates(): Response<States>

    @GET("api/v2/admin/location/districts/{id}")
    suspend fun getDistricts(@Path("id") id: Int): Response<Districts>

}