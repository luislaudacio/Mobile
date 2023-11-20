package com.example.projetointegrador

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetointegrador.adapters.AdapterFragmentSG
import com.example.projetointegrador.models.ImageItem
import com.example.projetointegrador.models.Post
import com.example.projetointegrador.models.Usuario
import com.example.projetointegrador.models.modalItem


class SeguindoFragment(private var posts: List<Post>, private var usuario: Usuario) : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterFragmentSG
    private var listaImagens: MutableList<modalItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_seguindo, container, false)
        recyclerView = view.findViewById(R.id.recyclerSG)

        configureAdapter()

        if (posts.isNotEmpty()) {
            listaImagens.clear()
            for (post in posts) {
                if (usuario.seguindo.contains(post.usuario)) {
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
            adapter.notifyDataSetChanged()
        }

        return view
    }

    private fun configureAdapter() {
        adapter = AdapterFragmentSG(requireContext(), listaImagens, usuario, this)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter
    }

    fun onFollowActionCompleted() {
        // Atualizar a lista e notificar o adaptador
        listaImagens.clear()
        for (post in posts) {
            if (usuario.seguindo.contains(post.usuario)) {
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
        adapter.notifyDataSetChanged()
    }
}

