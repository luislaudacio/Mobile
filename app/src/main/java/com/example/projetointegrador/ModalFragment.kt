package com.example.projetointegrador

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.projetointegrador.api.RetrofitClient
import com.example.projetointegrador.models.Usuario
import com.google.common.net.MediaType
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import java.io.File
import retrofit2.Response


class ModalFragment (private val usuario: Usuario): DialogFragment() {
    private val PICK_IMAGE_REQUEST = 1
    private var nomeImagemSelecionada: String? = null
    private lateinit var imagemSelecionada: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_modal, container, false)

        val btEscolherImagem: Button = view.findViewById(R.id.btEscolherImagem)
        val nomeFoto:TextView = view.findViewById(R.id.nomeFoto)
        val btPostar: Button = view.findViewById(R.id.btnPostar)

        btEscolherImagem.setOnClickListener{
            val galeriaIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriaIntent, PICK_IMAGE_REQUEST)
        }


        if (nomeImagemSelecionada != null) {
            nomeFoto.text = nomeImagemSelecionada
            nomeFoto.visibility = View.VISIBLE
        }

        btPostar.setOnClickListener{ enviarPost() }

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imagemSelecionada = data.data!!
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

    private fun enviarPost() {
        if (!::imagemSelecionada.isInitialized) {
            Log.i("ModalFragment", "Imagem não selecionada")
            return
        }

        val retrofitCli: RetrofitClient = RetrofitClient()

        val file: File? = obterArquivoDaUri(imagemSelecionada)

        if (file == null) {
            Log.e("ModalFragment", "Erro ao obter o arquivo da imagem selecionada")
            return
        }
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val call = retrofitCli.servicoUsuario.uploadImage(body, "Bearer ${usuario.access_token}")
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, infosResponse: Response<JsonObject>) {
                if (!infosResponse.isSuccessful) {
                    Log.e("ModalFragment", "response: " + infosResponse)
                    return
                }
                val responseBody = infosResponse.body()
                if (responseBody != null) {
                    Log.d("ModalFragment", "Upload da imagem bem-sucedido. Resposta: $responseBody")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("ModalFragment", "Falha na requisição: ${t.message}")
                return
            }
        })
    }

    private fun obterArquivoDaUri(uri: Uri): File? {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            inputStream?.use { input ->
                val bytes = input.readBytes()
                val file = File.createTempFile("imagem", null, requireContext().cacheDir)
                file.outputStream().use { output ->
                    output.write(bytes)
                }
                return file
            }
        } catch (e: Exception) {
            Log.e("ModalFragment", "Erro ao obter o arquivo da Uri: ${e.message}")
        }
        return null
    }



}