package com.example.projetointegrador

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


class SeguindoFragment (private var posts: List<Post>, private var usuario: Usuario) : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_seguindo, container, false)
        var listaImagens = mutableListOf<modalItem>()

        if (posts != null) {
            fun realizarValidacoes(post: Post) {
                if (!usuario.seguindo.contains(post.usuario)) {
                    return;
                }

                listaImagens.add(
                    modalItem(
                        image = ImageItem(post.pathFotoPost),
                        nomeUsuario = usuario.usuario,
                        tokenUsuario = usuario.access_token,
                        Post = post
                    )
                )
            }

            posts.forEach { post -> realizarValidacoes(post) }
        }


        val adapter = AdapterFragmentSG(requireContext(), listaImagens)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerSG)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        return view
    }
}