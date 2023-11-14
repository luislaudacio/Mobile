package com.example.projetointegrador

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.projetointegrador.adapters.OnPostInteractionListener
import com.example.projetointegrador.api.RetrofitClient
import com.example.projetointegrador.models.Post
import com.example.projetointegrador.models.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ModalPostagemFragment(
    private val buttonActive: Boolean,
    private var post: Post,
    private val nomeUsuarioLogado: String,
    private val token_usuario: String,
    private val onPostInteractionListener: OnPostInteractionListener
) : DialogFragment() {
    lateinit var iconHeart: ImageView
    lateinit var curtidas: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_modal_postagem, container, false)
        val buttonDeletar = view.findViewById<Button>(R.id.btnDeletar)
        val imagemModal = view.findViewById<ImageView>(R.id.fotoModal)
        val imageUrl = arguments?.getString("image_url")
        curtidas = view.findViewById<TextView>(R.id.numCurtidas)
        iconHeart = view.findViewById<ImageView>(R.id.iconHeart)


        curtidas.text = post.curtidas.size.toString()
        if (post.curtidas.contains(nomeUsuarioLogado)) {
            iconHeart.setColorFilter(Color.parseColor("#FF4081"))
            iconHeart.setOnClickListener {curtirFoto(true)}
        } else {
            iconHeart.setOnClickListener {curtirFoto(false)}
        }

        buttonDeletar.visibility = if (!buttonActive) View.INVISIBLE else View.VISIBLE

        if (buttonActive) {
            buttonDeletar.setOnClickListener {deletarPost()}
        }

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

    private fun deletarPost() {
        val retrofitCli: RetrofitClient = RetrofitClient()
        val call = retrofitCli.servicoUsuario.deletePost(post._id, "Bearer ${token_usuario}")
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, infosResponse: Response<Void>) {
                if (!infosResponse.isSuccessful) {
                    Log.e("Erro Desconhecido! 4", "etr: " + infosResponse)
                    return
                } else {
                    Log.e("Post Deletado", "Deletado" )
                    onPostInteractionListener.onPostDeleted(post)

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Falha ao deletar post", "onFailure: " + t)
                return
            }
        })
    }

    private fun curtirFoto (like: Boolean) {
        val retrofitCli: RetrofitClient = RetrofitClient()
        val call = retrofitCli.servicoUsuario.likePost(post._id, "Bearer ${token_usuario}", mapOf("usuario" to nomeUsuarioLogado))
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, infosResponse: Response<Void>) {
                if (!infosResponse.isSuccessful) {
                    Log.e("Erro Desconhecido! 3", "etr: " + infosResponse)
                    return
                } else {
                    if (like) {
                        val curtidasMutavel = post.curtidas.toMutableList()
                        curtidasMutavel.remove(nomeUsuarioLogado)
                        val novaListaCurtidas = curtidasMutavel.toList()
                        post.curtidas = novaListaCurtidas

                        Log.e("deslike!", "deslikeou!")
                        curtidas.text = post.curtidas.size.toString()
                        iconHeart.setColorFilter(Color.parseColor("#000000"))
                        iconHeart.setOnClickListener {curtirFoto(false)}
                        return;
                    }

                    val novaListaCurtidas = post.curtidas + nomeUsuarioLogado
                    post.curtidas = novaListaCurtidas
                    Log.e("like!", "Curtioooooo!")

                    curtidas.text = post.curtidas.size.toString()
                    iconHeart.setColorFilter(Color.parseColor("#FF4081"))
                    iconHeart.setOnClickListener {curtirFoto(true)}
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Falha ao dar like/deslike", "onFailure: " + t)
                return
            }
        })
    }
}