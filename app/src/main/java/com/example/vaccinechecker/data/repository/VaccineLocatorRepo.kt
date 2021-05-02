package com.example.vaccinechecker.data.repository

import android.app.Application
import com.example.vaccinechecker.data.api.ApiService
import com.example.vaccinechecker.data.api.BackendApis
import com.example.vaccinechecker.data.model.district.DistrictX
import com.example.vaccinechecker.data.model.district.Districts
import com.example.vaccinechecker.data.model.locations.VaccineLocationRes
import com.example.vaccinechecker.data.model.states.States
import com.google.gson.Gson

class VaccineLocatorRepo(context: Application)  {
    private var api: BackendApis? = null

    init {
        api = ApiService.getApiService(context)
    }

    companion object {
        private var ref: VaccineLocatorRepo? = null;
        fun getInstance(context: Application): VaccineLocatorRepo {
            if (ref != null) {
                return ref as VaccineLocatorRepo;
            }
            val instance = VaccineLocatorRepo(context);
            ref = instance
            return ref as VaccineLocatorRepo
        }
    }





    suspend fun getDataByDistrict(map: HashMap<String,String>): VaccineLocationRes? {

        val res = api?.getDataByDistrict(map)

        if (res?.isSuccessful == true) {
            return res.body();
        } else {
            return Gson().fromJson(res?.errorBody()?.charStream(), VaccineLocationRes::class.java)
        }
    }

    suspend fun getDataByPincode(map: HashMap<String,String>): VaccineLocationRes? {

        val res = api?.getDataByPincode(map)

        if (res?.isSuccessful == true) {
            return res.body();
        } else {
            return Gson().fromJson(res?.errorBody()?.charStream(), VaccineLocationRes::class.java)
        }
    }
    suspend fun getStates(): States? {

        val res = api?.getStates()

        if (res?.isSuccessful == true) {
            return res.body();
        } else {
            return Gson().fromJson(res?.errorBody()?.charStream(), States::class.java)
        }
    }
    suspend fun getDistricts(id: Int): Districts? {

        val res = api?.getDistricts(id)

        if (res?.isSuccessful == true) {
            return res.body();
        } else {
            return Gson().fromJson(res?.errorBody()?.charStream(), Districts::class.java)
        }
    }
}