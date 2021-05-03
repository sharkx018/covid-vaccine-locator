package com.example.vaccinechecker.data.model.locations

data class VaccineLocationRes(
    val centers: List<Center>? = null
)
data class Center(
    val block_name: String? = null,
    val center_id: Int? = null,
    val district_name: String? = null,
    val fee_type: String? = null,
    val from: String? = null,
    val lat: Int? = null,
    val long: Int? = null,
    val name: String? = null,
    val pincode: Int? = null,
    val sessions: List<Session>? = null,
    val state_name: String? = null,
    val to: String? = null
)
data class Session(
    val available_capacity: Float? = null,
    val date: String? = null,
    val min_age_limit: Int? = null,
    val session_id: String? = null,
    val slots: List<String>? = null,
    val vaccine: String? = null,
    var block_name: String? = null
)