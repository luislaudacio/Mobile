package com.example.projetointegrador.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario (
    var access_token: String,
    var email: String,
    var senha: String
):java.io.Serializable