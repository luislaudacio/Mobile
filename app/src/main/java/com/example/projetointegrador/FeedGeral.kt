package com.example.projetointegrador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.projetointegrador.adapters.ViewPagerAdapter
import com.example.projetointegrador.api.RetrofitClient
import com.example.projetointegrador.models.Post
import com.example.projetointegrador.models.Usuario
import com.example.projetointegrador.models.itemGetPost
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedGeral : AppCompatActivity() {

    lateinit var usuario: Usuario
    lateinit var posts : List<Post>


    override fun onCreate(savedInstanceState: Bundle?) {

        val extras = intent.extras
        if (extras != null) {
            usuario = getIntent().getSerializableExtra("usuario") as Usuario
        }


        posts = mutableListOf<Post>()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_geral)

        val textoPerfil: TextView = findViewById(R.id.textoPerfil)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        textoPerfil.text = usuario.usuario

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.pink))

        val retrofitCli: RetrofitClient = RetrofitClient()
        val call = retrofitCli.servicoUsuario.getAllPosts("Bearer ${usuario.access_token}")
        call.enqueue(object : Callback<List<itemGetPost>> {
            override fun onResponse(call: Call<List<itemGetPost>>, postsResponse: Response<List<itemGetPost>>) {
                if (!postsResponse.isSuccessful) {
                    Log.e("Erro Desconhecido 2!", "response: " + postsResponse)
                    Toast.makeText(this@FeedGeral, "Ocorreu um erro ao buscar o Feed, tente Novamente mais tarde.", Toast.LENGTH_LONG).show()
                    configTabLayout()
                    return
                }

                val infoResponse = postsResponse.body()
                if (infoResponse != null) {
                    Log.i("Informacoes Usuario", "onResponse: " + infoResponse.toString())

                    fun realizarValidacoes(item: itemGetPost) {
                        item.posts.forEach { post -> posts += post}
                    }

                    infoResponse.forEach { item ->
                        realizarValidacoes(item)
                    }

                }

                configTabLayout()

            }

            override fun onFailure(call: Call<List<itemGetPost>>, t: Throwable) {
                Log.e("Falha ao buscar info", "onFailure: " + t)
                Toast.makeText(this@FeedGeral, "Falha ao buscar info", Toast.LENGTH_LONG).show()
                return
            }
        })



    }

    private fun configTabLayout() {
        val adapter: ViewPagerAdapter = ViewPagerAdapter(this)
        val ConteudoViewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)

        ConteudoViewPager.adapter = adapter

        adapter.addFragment(FeedGeralFragment(posts, usuario))
        adapter.addFragment(SeguindoFragment(posts, usuario))


        val mediator = TabLayoutMediator(tabLayout, ConteudoViewPager) {
            tab, position ->

            when (position) {
                0 -> tab.text = "Feed Geral"
                1 -> tab.text = "Seguindo"

                else -> tab.text = "Aba Padrão"
            }
        }.attach()


    }

    fun redMinhaConta(view: View) {
        var intent: Intent = Intent(this, MinhaConta:: class.java)
        intent.putExtra("usuario", usuario)
        startActivity(intent)
    }



}