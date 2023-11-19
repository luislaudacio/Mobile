package com.example.projetointegrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetointegrador.adapters.AdapterFragment
import com.example.projetointegrador.models.ImageItem
import com.example.projetointegrador.models.Post
import com.example.projetointegrador.models.Usuario
import com.example.projetointegrador.models.modalItem


class FeedGeralFragment (private var posts: List<Post>, private var usuario: Usuario) : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed_geral, container, false)
        var listaImagens = mutableListOf<modalItem>()

        if (posts != null) {
            posts.forEach { post ->
                listaImagens.add(
                    modalItem(
                        image = ImageItem(post.pathFotoPost),
                        nomeUsuario = usuario.usuario,
                        tokenUsuario = usuario.access_token,
                        Post = post
                    )
                )
            }
        }

        val adapter = AdapterFragment(requireContext(), listaImagens, usuario)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerFG)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        return view
    }
}