package com.example.projetointegrador.api

import com.example.projetointegrador.models.Usuario

import retrofit2.Call
import retrofit2.http.*

interface ServicoUsuario {

    @POST("auth/login")
    fun getToken(@Body usuario: Usuario): Call<Usuario>

    @POST("user/register")
    fun userRegister(@Body usuario: Usuario): Call<Usuario>

    @GET("user/email/{email}")
    fun getUserInfo(@Path("email") email: String, @Header("Authorization") token: String): Call<Usuario>
//
//    @GET("contatos")
//    fun getAllContatos(): Call<List<Contato>>
//
//    @POST("contatos")
//    fun createContato(@Body contato: Contato): Call<Contato>
//
//    @PUT("contatos/{id}")
//    fun updateContato(@Path("id") id: Int, @Body contato: Contato): Call<Contato>
//
//    @DELETE("contatos/{id}")
//    fun deleteContato(@Path("id") id: Int): Call<Void>
}

