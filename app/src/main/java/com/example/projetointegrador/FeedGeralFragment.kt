package com.example.projetointegrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetointegrador.adapters.AdapterFragment
import com.example.projetointegrador.adapters.AdapterMConta
import com.example.projetointegrador.models.ImageItem


class FeedGeralFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed_geral, container, false)

        val listaImagens = listOf(
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
            ImageItem("https://i.pinimg.com/originals/49/ce/25/49ce259a44c02bb4deb037dac8b9a54a.jpg"),
        )

        val adapter = AdapterFragment(requireContext(), listaImagens)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerFG)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        return view
    }
}