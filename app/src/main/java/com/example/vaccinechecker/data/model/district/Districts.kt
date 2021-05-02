package com.example.vaccinechecker.data.model.district

data class Districts(
    val districts: List<DistrictX>? = null,
    val ttl: Int? = null
)

data class DistrictX(
    val district_id: Int? = null,
    val district_name: String? = null
)