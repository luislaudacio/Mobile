package com.example.projetointegrador

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetointegrador.NavigationUtils.Navigation
import com.example.projetointegrador.adapters.AdapterMConta
import com.example.projetointegrador.models.ImageItem
import com.example.projetointegrador.models.Usuario
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

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

        val listaImagens = mutableListOf<ImageItem>()

        if (usuario.posts != null) {
            usuario.posts.forEach { post ->
                listaImagens.add(ImageItem(post.pathFotoPost))
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
            val modalFragment = ModalFragment()
            modalFragment.show(supportFragmentManager, "MEU MODAL")
        }
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
