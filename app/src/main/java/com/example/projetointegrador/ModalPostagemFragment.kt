package com.example.projetointegrador

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide


class ModalPostagemFragment : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_modal_postagem, container, false)

        val imagemModal = view.findViewById<ImageView>(R.id.fotoModal)

        // Recupere a URL da imagem do argumento
        val imageUrl = arguments?.getString("image_url")

        // Carregue a imagem usando uma biblioteca como Glide
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .into(imagemModal)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels * 1.00).toInt()
        val height = (resources.displayMetrics.widthPixels * 1.80).toInt()
        dialog?.window?.setLayout(width, height)
    }


}