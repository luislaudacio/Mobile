package com.example.projetointegrador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetointegrador.adapters.AdapterMConta
import com.example.projetointegrador.models.ImageItem
import com.example.projetointegrador.models.Usuario
import com.example.projetointegrador.models.modalItem

class MinhaConta : AppCompatActivity() {
    lateinit var usuario: Usuario
    lateinit var nomeMinhaConta: TextView
    lateinit var adapter: AdapterMConta
    var listaImagens: MutableList<modalItem> = mutableListOf<modalItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.minha_conta)

        val extras = intent.extras
        if (extras != null) {
            usuario = getIntent().getSerializableExtra("usuario") as Usuario
        }

        nomeMinhaConta = findViewById(R.id.nomeMinhaConta)
        nomeMinhaConta.text = usuario.usuario

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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerImgs)
        adapter = AdapterMConta(usuario, this@MinhaConta, listaImagens, recyclerView)
        val addFotos: ImageButton = findViewById(R.id.addFotos)
        val sairConta: ImageButton = findViewById(R.id.sairConta)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        sairConta.setOnClickListener{sairContaUsuario()}
        addFotos.setOnClickListener{
            val modalFragment = ModalFragment(usuario, adapter)
            modalFragment.show(supportFragmentManager, "MEU MODAL")
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.atualizarPostsNaAPI(
            onSuccess = { posts ->
                if (posts != null) {
                    usuario.posts = posts
                }
                listaImagens = mutableListOf<modalItem>()
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

                adapter.atualizarListaImagens(listaImagens)
                adapter.notifyDataSetChanged()

            },
            onFailure =  { t ->
                Log.e("Erro na atualização", "onFailure: " + t)
                Toast.makeText(this@MinhaConta, "Erro ao buscar posts", Toast.LENGTH_LONG).show()
            }
        )
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
