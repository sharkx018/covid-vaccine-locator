package com.example.vaccinechecker.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.vaccinechecker.data.model.district.Districts
import com.example.vaccinechecker.data.model.locations.VaccineLocationRes
import com.example.vaccinechecker.data.model.states.States
import com.example.vaccinechecker.data.repository.VaccineLocatorRepo
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch

class MainViewModel(context: Application) : AndroidViewModel(context) {

    private val repo = VaccineLocatorRepo.getInstance(context);
    var vaccineLocationResponse = MutableLiveData<VaccineLocationRes>();
    var statesResponse = MutableLiveData<States>();
    var districtsResponse = MutableLiveData<Districts>();
    var loading = MutableLiveData<Boolean>();


    @RequiresApi(Build.VERSION_CODES.M)
    fun getDataByPincode(query: HashMap<String, String>) {
        viewModelScope.launch {
            if (hasInternetConnection()) {
                try {
                    loading.value = true;
                    vaccineLocationResponse.value = repo.getDataByPincode(query)
                    loading.value = false;
                    Log.d("log", GsonBuilder().setPrettyPrinting().create().toJson(vaccineLocationResponse.value));
                } catch (e: Exception) {
                    loading.value = false;
                    Log.d("kkk", e.message.toString());
                    Toast.makeText(getApplication(), "Catch: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun getDataByDistrict(query: HashMap<String, String>) {
        viewModelScope.launch {
            if (hasInternetConnection()) {
                try {
                    loading.value = true;
                    vaccineLocationResponse.value = repo.getDataByDistrict(query)
                    loading.value = false;
                    Log.d("log", GsonBuilder().setPrettyPrinting().create().toJson(vaccineLocationResponse.value));
                } catch (e: Exception) {
                    loading.value = false;
                    Log.d("kkk", e.message.toString());
                    Toast.makeText(getApplication(), "Catch: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getStates() {
        viewModelScope.launch {
            if (hasInternetConnection()) {
                try {
                    statesResponse.value = repo.getStates()
                    Log.d("log", GsonBuilder().setPrettyPrinting().create().toJson(statesResponse.value));
                } catch (e: Exception) {
                    Toast.makeText(getApplication(), "Catch: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getDistrictsByStates(id: Int) {
        viewModelScope.launch {
            if (hasInternetConnection()) {
                try {
                    districtsResponse.value = repo.getDistricts(id)
                } catch (e: Exception) {
                    Toast.makeText(getApplication(), "Catch: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false;
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false;

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }

}