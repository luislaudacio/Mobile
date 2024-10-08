package com.example.projetointegrador.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import java.util.Date

@Serializable
data class Usuario (
    var _id: String,
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
    var pathFotoPost: String,
    var descricaoPost: String,
    var comentarios: List<Comentario>,
    var tags: List<String>,
    var curtidas: List<String>,
    var criadoEm: String,
    var atualizadoEm: String,
    var _id: String
):java.io.Serializable

@Serializable
data class Comentario(
    val usuario: String,
    val comentarioTexto: String,
    val criadoEm: String,
    val atualizadoEm: String,
    val _id: String
):java.io.Serializable

@Serializable
data class modalItem(
    var image: ImageItem,
    var nomeUsuario: String,
    var Post: Post,
    var tokenUsuario: String
):java.io.Serializable

@Serializable
data class itemGetPost(
    val _id: String,
    val usuario: String,
    val posts: List<Post>,
):java.io.Serializable

