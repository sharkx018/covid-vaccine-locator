package com.example.vaccinechecker.data.model.states

data class States(
    val states: List<State>? = null,
    val ttl: Int? = null
)
data class State(
    val state_id: Int? = null,
    val state_name: String? = null
)