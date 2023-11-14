package com.example.projetointegrador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetointegrador.adapters.AdapterMConta
import com.example.projetointegrador.api.RetrofitClient
import com.example.projetointegrador.models.ImageItem
import com.example.projetointegrador.models.Post
import com.example.projetointegrador.models.Usuario
import com.example.projetointegrador.models.modalItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MinhaConta : AppCompatActivity() {
    lateinit var usuario: Usuario
    lateinit var nomeMinhaConta: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.minha_conta)

        val extras = intent.extras
        if (extras != null) {
            usuario = getIntent().getSerializableExtra("usuario") as Usuario
        }

        nomeMinhaConta = findViewById(R.id.nomeMinhaConta)
        nomeMinhaConta.text = usuario.usuario

        var listaImagens = mutableListOf<modalItem>()

        if (usuario.posts != null) {
            usuario.posts.forEach { post ->
                listaImagens.add(modalItem(
                    image = ImageItem(post.pathFotoPost),
                    nomeUsuario = usuario.usuario,
                    tokenUsuario = usuario.access_token,
                    Post = post
                ))
            }
        }

        val adapter = AdapterMConta(this, listaImagens)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerImgs)
        val minhaContaTexto= findViewById<TextView>(R.id.MinhaContaText)
        val addFotos: ImageButton = findViewById(R.id.addFotos)
        val sairConta: ImageButton = findViewById(R.id.sairConta)


        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter


        sairConta.setOnClickListener{sairContaUsuario()}


        addFotos.setOnClickListener{
            val modalFragment = ModalFragment(usuario)
            modalFragment.show(supportFragmentManager, "MEU MODAL")
        }
    }

    override fun onResume() {
        super.onResume()

        val retrofitCli: RetrofitClient = RetrofitClient()
        val call = retrofitCli.servicoUsuario.getUserInfo(usuario.email, "Bearer ${usuario.access_token}")
        call.enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, infosResponse: Response<Usuario>) {
                if (!infosResponse.isSuccessful) {
                    Log.e("Erro Desconhecido!", "response: " + infosResponse)
                    return
                }

                val infoResponse = infosResponse.body()
                if (infoResponse != null) {
                    Log.i("Informacoes Usuario", "onResponse: " + infoResponse.toString())
                    usuario.criadoEm = infoResponse.criadoEm
                    usuario.usuario = infoResponse.usuario
                    usuario.dataNasc = infoResponse.dataNasc
                    usuario.seguidores = infoResponse.seguidores
                    usuario.seguindo = infoResponse.seguindo

                    if (infoResponse.posts != null) {
                        usuario.posts = infoResponse.posts
                    }


                    setContentView(R.layout.minha_conta)

                    val extras = intent.extras
                    if (extras != null) {
                        usuario = getIntent().getSerializableExtra("usuario") as Usuario
                    }

                    nomeMinhaConta = findViewById(R.id.nomeMinhaConta)
                    nomeMinhaConta.text = usuario.usuario

                    var listaImagens = mutableListOf<modalItem>()

                    if (usuario.posts != null) {
                        usuario.posts.forEach { post ->
                            listaImagens.add(modalItem(
                                image = ImageItem(post.pathFotoPost),
                                nomeUsuario = usuario.usuario,
                                tokenUsuario = usuario.access_token,
                                Post = post
                            ))
                        }
                    }

                    val adapter = AdapterMConta(this@MinhaConta, listaImagens)
                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerImgs)
                    val minhaContaTexto= findViewById<TextView>(R.id.MinhaContaText)
                    val addFotos: ImageButton = findViewById(R.id.addFotos)
                    val sairConta: ImageButton = findViewById(R.id.sairConta)


                    recyclerView.layoutManager = GridLayoutManager(this@MinhaConta, 2)
                    recyclerView.adapter = adapter


                    sairConta.setOnClickListener{sairContaUsuario()}


                    addFotos.setOnClickListener{
                        val modalFragment = ModalFragment(usuario)
                        modalFragment.show(supportFragmentManager, "MEU MODAL")
                    }
               }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Log.e("Falha ao buscar info", "onFailure: " + t)
                return
            }
        })
    }

    private fun sairContaUsuario() {
        var intent: Intent = Intent(this, MainActivity:: class.java)
        startActivity(intent)
    }

    fun feedGer(view: View) {
        var intent: Intent = Intent(this, FeedGeral:: class.java)
        intent.putExtra("usuario", usuario)
        startActivity(intent)
    }

}
