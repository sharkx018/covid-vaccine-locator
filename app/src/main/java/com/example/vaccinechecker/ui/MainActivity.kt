package com.example.vaccinechecker.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vaccinechecker.data.model.locations.Session
import com.example.vaccinechecker.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap


enum class QueryType {
    SearchByPincode,
    SearchByDistrict
}

class MainActivity : AppCompatActivity() {

    lateinit var viewmodel: MainViewModel;
    lateinit var binding: ActivityMainBinding;
    lateinit var stateAdaptor: ArrayAdapter<String>
    lateinit var districtAdaptor: ArrayAdapter<String>
    lateinit var centerAdaptor: ArrayAdapter<String>
    var sLists = mutableListOf<String>();
    var dList = mutableListOf<String>();
    var cList = mutableListOf<String>();
    var districtId = -1;
    var flag = QueryType.SearchByPincode;
    private var mYear = 0
    private  var mMonth:Int = 0
    private  var mDay:Int = 0

    var allSessions = mutableListOf<Session>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
//        setContentView(R.layout.activity_main)

        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java);

        setupRecyclerView();

        val queryMap = HashMap<String, String>();

        binding.toggleLabel.setOnClickListener {
            districtId  =-1;
            queryMap["pincode"] = ""
            queryMap["district_id"] = ""
            queryMap["date"] = ""
            binding.districtLayout.dateLabel.text = "Date: "
            binding.pincodeLayout.date.text = "Date: "
            when (flag) {
                QueryType.SearchByPincode -> {
                    flag = QueryType.SearchByDistrict
                    binding.districtLinearLayout.visibility = View.VISIBLE;
                    binding.pincodeLinearLayout.visibility = View.GONE;
                    binding.toggleLabel.text = "Click here to search by pincode";

                }
                QueryType.SearchByDistrict -> {
                    flag = QueryType.SearchByPincode
                    binding.districtLinearLayout.visibility = View.GONE;
                    binding.pincodeLinearLayout.visibility = View.VISIBLE;
                    binding.toggleLabel.text = "Click here to search by district";
                }
            }
        }

        binding.pincodeLayout.date.setOnClickListener{
            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    queryMap["date"] = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year;
                    binding.pincodeLayout.date.text = "Date: "+ dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        binding.districtLayout.dateLabel.setOnClickListener{
            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    queryMap["date"] = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year;
                    binding.districtLayout.dateLabel.text = "Date: "+ dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        binding.findBtn.setOnClickListener {

            if(queryMap.containsKey("date") == false || queryMap["date"] == ""){
                Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            when (flag) {
                QueryType.SearchByPincode -> {
                    Log.d("query", queryMap.toString());
                    val pincode = binding.pincodeLayout.pincodeEditText.text.toString();
                    if (isValidPincode(pincode)) {
                        queryMap["pincode"] = pincode;
                        (binding.recyclerView.adapter as SessionAdaptor).submitList(mutableListOf());
                        viewmodel.getDataByPincode(queryMap)
                    } else {
                        Toast.makeText(this, "Enter valid pincode", Toast.LENGTH_SHORT).show();
                    }
                }
                QueryType.SearchByDistrict -> {
                    Log.d("query", queryMap.toString());

                    if (districtId == -1) {
                        Toast.makeText(this, "Please select the distict", Toast.LENGTH_SHORT)
                            .show();
                    } else {
                        queryMap["district_id"] = districtId.toString();
                        viewmodel.getDataByDistrict(queryMap);
                    }
                }
            }


        }





        viewmodel.loading.observe(this) {
            if (it == false) {
                binding.progressBar.visibility = View.GONE;
            } else if (it == true) {
                binding.progressBar.visibility = View.VISIBLE;
            }
        }

        viewmodel.getStates()
        viewmodel.statesResponse.observe(this) {
            sLists = mutableListOf<String>();
            it.states?.forEachIndexed { index, state ->
                sLists.add(state.state_name!!);
            }
            stateAdaptor = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sLists)
            binding.districtLayout.stateSpinner.adapter = stateAdaptor
        }


        binding.districtLayout.stateSpinner.setOnItemSelectedListener(object :
            OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                districtId = -1;
                viewmodel.getDistrictsByStates(position);
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })


        viewmodel.districtsResponse.observe(this) {
            dList = mutableListOf();
            it.districts?.forEachIndexed { index, districtX ->
                dList.add(districtX.district_name!!)
            }
            districtAdaptor = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dList)
            binding.districtLayout.districtSpinner.adapter = districtAdaptor

        }
        binding.districtLayout.districtSpinner.setOnItemSelectedListener(object :
            OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                districtId = viewmodel.districtsResponse.value?.districts?.get(position)?.district_id ?: -1;
                queryMap["district_id"] = districtId.toString();
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })

        viewmodel.vaccineLocationResponse.observe(this) {

            allSessions = mutableListOf()
            cList = mutableListOf()

            if(it.centers?.size == 0){
                Toast.makeText(this, "Center not found, Sorry...", Toast.LENGTH_SHORT).show();
                return@observe
            }

            // center name spinner
            cList.add(0, "All Centres");
            it.centers?.forEachIndexed { index, center ->
                cList.add(center.name!!);
            }
            centerAdaptor = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cList);
            binding.vaccineCenterSpinner.adapter = centerAdaptor;

            it.centers?.forEachIndexed { index, center ->
                center.sessions?.forEach { session ->
                    session.block_name = center.name
                    session.pincode = center.pincode
                    allSessions.add(session)
                }
            }

        }
        binding.vaccineCenterSpinner.setOnItemSelectedListener(object :
            OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {

                val age = binding.ageEditText.text.toString()
                if(age.length > 0)
                {
                    var ageVal: Int;
                    try {
                        ageVal = age.toInt();
                    }catch (e:Exception){
                        Toast.makeText(this@MainActivity, "Please enter valid age", Toast.LENGTH_SHORT).show();
                        return
                    }

                    if( position == 0){
                        allSessions = allSessions.filter { session ->
                            ageVal >= session.min_age_limit!! && session.available_capacity!! > 0
                        }.toMutableList()
                        if(allSessions.size == 0){
                            binding.noDataFound.visibility = View.VISIBLE;
                        }else{
                            binding.noDataFound.visibility = View.GONE;
                        }
                        (binding.recyclerView.adapter as SessionAdaptor).submitList(allSessions);
                    }else{

                        var list = viewmodel.vaccineLocationResponse.value?.centers?.get(position-1)?.sessions
                        if (list != null) {

                            list = list.filter { session ->
                                ageVal >= session.min_age_limit!! && session.available_capacity!! > 0
                            }.toMutableList()

                            if(list.size == 0){
                                binding.noDataFound.visibility = View.VISIBLE;
                            }else{
                                binding.noDataFound.visibility = View.GONE;
                            }
                            (binding.recyclerView.adapter as SessionAdaptor).submitList(list);

                        }
                    }

                }else{
                    if( position == 0){
                        if(allSessions.size == 0){
                            binding.noDataFound.visibility = View.VISIBLE;
                        }else{
                            binding.noDataFound.visibility = View.GONE;
                        }
                        (binding.recyclerView.adapter as SessionAdaptor).submitList(allSessions);
                    }else{
                        val list = viewmodel.vaccineLocationResponse.value?.centers?.get(position-1)?.sessions
                        if(list!!.size == 0){
                            binding.noDataFound.visibility = View.VISIBLE;
                        }else{
                            binding.noDataFound.visibility = View.GONE;
                        }
                        (binding.recyclerView.adapter as SessionAdaptor).submitList(list);
                    }
                }


            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })


    }

    private fun isValidPincode(pincode: String): Boolean {

        val regex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(pincode)
        return matcher.matches()
    }


    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this);
        binding.recyclerView.adapter = SessionAdaptor();

    }
}