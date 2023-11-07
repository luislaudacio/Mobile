package com.example.projetointegrador.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import java.util.Date

@Serializable
data class Usuario (
    var message: String,
    var access_token: String,
    var email: String,
    var senha: String,
    var usuario: String,
    var dataNasc: String,
    var dataAtual : String,
    var seguidores: Array<String>,
    var seguindo: Array<String>,
    var criadoEm: String,
    var posts: List<Post> // @Transient
):java.io.Serializable

@Serializable
data class Post(
    val usuario: String,
    val pathFotoPost: String,
    val descricaoPost: String,
    val comentarios: List<Comentario>,
    val tags: List<String>,
    val curtidas: List<String>,
    val criadoEm: String,
    val atualizadoEm: String,
    val _id: String
):java.io.Serializable

@Serializable
data class Comentario(
    val usuario: String,
    val comentarioTexto: String,
    val criadoEm: String,
    val atualizadoEm: String,
    val _id: String
):java.io.Serializable