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
import com.example.projetointegrador.models.Usuario


class SeguindoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_seguindo, container, false)

        val listaImagens = listOf(
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
        )

        val adapter = AdapterFragmentSG(requireContext(), listaImagens)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerSG)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        return view
    }
}