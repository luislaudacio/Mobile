package com.example.projetointegrador

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment


class ModalFragment : DialogFragment() {
    private val PICK_IMAGE_REQUEST = 1
    private var nomeImagemSelecionada: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_modal, container, false)

        val btEscolherImagem: Button = view.findViewById(R.id.btEscolherImagem)
        val nomeFoto:TextView = view.findViewById(R.id.nomeFoto)


        btEscolherImagem.setOnClickListener{
            val galeriaIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriaIntent, PICK_IMAGE_REQUEST)
        }


        if (nomeImagemSelecionada != null) {
            nomeFoto.text = nomeImagemSelecionada
            nomeFoto.visibility = View.VISIBLE
        }



        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imagemSelecionada = data.data
            nomeImagemSelecionada = obterNomeImagem(imagemSelecionada)

            if (nomeImagemSelecionada != null) {
                val nomeFoto: TextView? = view?.findViewById(R.id.nomeFoto)
                nomeFoto?.text = nomeImagemSelecionada
                nomeFoto?.visibility = View.VISIBLE
            }

        }
    }

    private fun obterNomeImagem(imagemSelecionada: Uri?): String? {
        if (imagemSelecionada != null) {
            val cursor = context?.contentResolver?.query(imagemSelecionada, null, null, null, null)

            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                it.moveToFirst()
                return it.getString(nameIndex)
            }
        }
        return "Nome Desconhecido"
    }

    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels * 1.00).toInt()
        val height = (resources.displayMetrics.widthPixels * 1.00).toInt()
        dialog?.window?.setLayout(width, height)
    }


}