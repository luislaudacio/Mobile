    package com.example.projetointegrador.api

    import com.example.projetointegrador.models.Post
    import com.example.projetointegrador.models.Usuario
    import com.example.projetointegrador.models.itemGetPost
    import com.google.gson.JsonElement
    import com.google.gson.JsonObject
    import okhttp3.MultipartBody
    import okhttp3.RequestBody

    import retrofit2.Call
    import retrofit2.http.*

    interface ServicoUsuario {

        @POST("auth/login")
        fun getToken(@Body usuario: Usuario): Call<Usuario>

        @POST("user/register")
        fun userRegister(@Body body: Map<String, String>): Call<Usuario>

        @GET("user/email/{email}")
        fun getUserInfo(@Path("email") email: String, @Header("Authorization") token: String): Call<Usuario>

        @GET("post/list")
        fun getAllPosts(@Header("Authorization") token: String): Call<List<itemGetPost>>

        @POST("post/like/{idPost}")
        fun likePost(@Path("idPost") idPost: String, @Header("Authorization") token: String, @Body body: Map<String, String>): Call<Void>

        @DELETE("post/remove/{idPost}")
        fun deletePost(@Path("idPost") idPost: String, @Header("Authorization") token: String): Call<Void>

        @Multipart
        @POST("post/upload")
        fun uploadImage(
            @Part file: MultipartBody.Part,
            @Header("Authorization") token: String
        ): Call<JsonObject>

        @POST("post/create/{idUsuario}")
        fun createPost(@Path("idUsuario") idUsuario: String, @Header("Authorization") token: String, @Body body: Map<String, String>): Call<JsonObject>

        @POST("comment/create/{idPost}")
        fun createComment(@Path("idPost") idPost: String, @Header("Authorization") token: String, @Body body: Map<String, String>): Call<JsonObject>


    }

