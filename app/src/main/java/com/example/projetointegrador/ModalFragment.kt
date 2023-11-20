package com.example.projetointegrador

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.projetointegrador.api.RetrofitClient
import com.example.projetointegrador.models.Usuario
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import java.io.File
import retrofit2.Response
import android.Manifest
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.projetointegrador.adapters.AdapterMConta
import com.example.projetointegrador.models.Post

class ModalFragment (
    private val usuario: Usuario,
    private val adapterMC: AdapterMConta
): DialogFragment() {
    private val PICK_IMAGE_REQUEST = 1
    private var nomeImagemSelecionada: String? = null
    private lateinit var imagemSelecionada: Uri
    private var MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    private lateinit var comentarioTexto: EditText
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
        }


        Log.i("Permissão", "onCreateView chamado")
        val view = inflater.inflate(R.layout.fragment_modal, container, false)

        val btEscolherImagem: Button = view.findViewById(R.id.btEscolherImagem)
        val nomeFoto:TextView = view.findViewById(R.id.nomeFoto)
        val btPostar: Button = view.findViewById(R.id.btnPostar)
        comentarioTexto = view.findViewById(R.id.comentarioTexto)

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
        val height = (resources.displayMetrics.heightPixels * 0.60).toInt()
        dialog?.window?.setLayout(width, height)
    }

    private fun enviarPost() {
        if (!::imagemSelecionada.isInitialized) {
            Log.i("ModalFragment", "Imagem não selecionada")
            Toast.makeText(requireContext(), "Você não selecionou a imagem!", Toast.LENGTH_LONG).show()
            return
        }

        val retrofitCli: RetrofitClient = RetrofitClient()
        try {
            val cacheDir =  requireContext().cacheDir
            val file: File? = File(cacheDir, nomeImagemSelecionada)

            if (file == null) {
                Log.e("ModalFragment", "Erro ao obter o arquivo da imagem selecionada")
                return
            }

            val inputStream = requireContext().contentResolver.openInputStream(imagemSelecionada)

            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val type = requireContext().contentResolver.getType(imagemSelecionada)
            val requestFile = file.asRequestBody(type?.toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val call = retrofitCli.servicoUsuario.uploadImage(body, "Bearer ${usuario.access_token}")
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    infosResponse: Response<JsonObject>
                ) {
                    if (!infosResponse.isSuccessful) {
                        Log.e("ModalFragment", "response: " + infosResponse)
                        return
                    }

                    val responseBody = infosResponse.body()
                    if (responseBody != null) {
                        val imageUrl = responseBody.getAsJsonPrimitive("url").asString
                        Log.d("ModalFragment",  "Upload da imagem bem-sucedido. Resposta: ${responseBody}")

                        val retrofitCli: RetrofitClient = RetrofitClient()
                        val call = retrofitCli.servicoUsuario.createPost(usuario._id, "Bearer ${usuario.access_token}", mapOf(
                            "usuario" to usuario.usuario,
                            "pathFotoPost" to imageUrl,
                            "descricaoPost" to comentarioTexto.text.toString()
                        ))

                        call.enqueue(object : Callback<JsonObject> {

                            override fun onResponse(call: Call<JsonObject>, criarPost: Response<JsonObject>) {
                                if (!criarPost.isSuccessful) {
                                    Log.e("Criar POST", "Erro ao Criar Post: " + criarPost)
                                    Toast.makeText(requireContext(), "Erro ao Criar Post", Toast.LENGTH_LONG).show()
                                    return
                                }

                                val criarPostBody = criarPost.body()
                                if (criarPostBody != null) {
                                    Log.e("Criar POST",  "Post Criado: ${criarPostBody}")
                                    Toast.makeText(requireContext(), "Post Criado!", Toast.LENGTH_LONG).show()

                                    val newPost = Post(
                                        usuario = usuario.usuario,
                                        pathFotoPost = imageUrl,
                                        descricaoPost = comentarioTexto.text.toString(),
                                        _id = "",
                                        atualizadoEm = "",
                                        criadoEm = "",
                                        comentarios = emptyList(),
                                        curtidas = emptyList(),
                                        tags = emptyList()
                                    )
                                    adapterMC.onPostUploaded(newPost)

                                    dismiss()
                                    return
                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                Toast.makeText(requireContext(), "Erro ao Criar Post", Toast.LENGTH_LONG).show()
                                Log.e("Criar POST", "Erro ao criar post: " + t)
                                return
                            }
                        })

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(requireContext(), "Erro ao Criar Post", Toast.LENGTH_LONG).show()
                    Log.e("ModalFragment", "Falha na requisição: ${t.message}")
                    return
                }
            })
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Erro ao mandar iamgem para o servidor!", Toast.LENGTH_LONG).show()
            Log.e("ModalFragment", "Erro ao processar a imagem: ${e.message}")
        }
    }
}