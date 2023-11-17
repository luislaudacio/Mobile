package com.example.projetointegrador.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    //    private val BASE_URL = "https://web-q5cgaeb0c6i6.up-de-fra1-1.apps.run-on-seenode.com/"
    private val BASE_URL =  "https://api-site-imagens.onrender.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val servicoUsuario: ServicoUsuario = retrofit.create(ServicoUsuario::class.java)


}