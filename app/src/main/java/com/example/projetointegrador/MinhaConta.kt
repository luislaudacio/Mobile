package com.example.projetointegrador

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetointegrador.adapters.AdapterMConta
import com.example.projetointegrador.models.ImageItem

class MinhaConta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.minha_conta)

        val listaImagens = listOf(
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            )

        val adapter = AdapterMConta(this, listaImagens)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerImgs)
        val minhaContaTexto= findViewById<TextView>(R.id.MinhaContaText)


        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter



    }
}
