package com.example.projetointegrador.api

import com.example.projetointegrador.models.Post
import com.example.projetointegrador.models.Usuario
import com.example.projetointegrador.models.itemGetPost

import retrofit2.Call
import retrofit2.http.*

interface ServicoUsuario {

    @POST("auth/login")
    fun getToken(@Body usuario: Usuario): Call<Usuario>

    @POST("user/register")
    fun userRegister(@Body usuario: Usuario): Call<Usuario>

    @GET("user/email/{email}")
    fun getUserInfo(@Path("email") email: String, @Header("Authorization") token: String): Call<Usuario>

    @GET("post/list")
    fun getAllPosts(@Header("Authorization") token: String): Call<List<itemGetPost>>

    @POST("post/like/{idPost}")
    fun likePost(@Path("idPost") idPost: String, @Header("Authorization") token: String, @Body body: Map<String, String>): Call<Void>

}

