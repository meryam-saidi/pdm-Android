package tn.esprit.pdm.network.model

data class LoginRequest(
    val email: String,
    val password: String
)