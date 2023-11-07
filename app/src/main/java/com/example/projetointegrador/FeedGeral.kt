package com.example.projetointegrador

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.projetointegrador.adapters.ViewPagerAdapter
import com.example.projetointegrador.models.Usuario
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class FeedGeral : AppCompatActivity() {

    lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {

        val extras = intent.extras
        if (extras != null) {
            usuario = getIntent().getSerializableExtra("usuario") as Usuario
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_geral)

        val tabLayout = findViewById<TabLayout>(R.id.tabs)

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.pink))


        configTabLayout()

    }

    private fun configTabLayout() {
        val adapter: ViewPagerAdapter = ViewPagerAdapter(this)
        val ConteudoViewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)

        ConteudoViewPager.adapter = adapter

        adapter.addFragment(FeedGeralFragment())
        adapter.addFragment(SeguindoFragment())


        val mediator = TabLayoutMediator(tabLayout, ConteudoViewPager) {
            tab, position ->

            when (position) {
                0 -> tab.text = "Feed Geral"
                1 -> tab.text = "Seguindo"

                else -> tab.text = "Aba Padrão"
            }
        }.attach()


    }



}