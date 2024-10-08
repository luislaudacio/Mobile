package com.example.projetointegrador

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.BoringLayout
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projetointegrador.adapters.AdapterComentarios
import com.example.projetointegrador.services.OnPostInteractionListener
import com.example.projetointegrador.api.RetrofitClient
import com.example.projetointegrador.models.Comentario
import com.example.projetointegrador.models.Post
import com.example.projetointegrador.models.Usuario
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModalPostagemFragment(
    private val buttonActive: Boolean,
    private var usuario: Usuario,
    private var post: Post,
    private val nomeUsuarioLogado: String,
    private val token_usuario: String,
    private val onPostInteractionListener: OnPostInteractionListener,
    private var fragment: SeguindoFragment? = null
) : DialogFragment() {
    lateinit var iconHeart: ImageView
    lateinit var curtidas: TextView
    lateinit var addSeguindo: ImageView
    lateinit var textoComentario : EditText
    lateinit var comentariosArtificiais: MutableList<Comentario>
    private lateinit var adapterComentarios: AdapterComentarios

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_modal_postagem, container, false)
        val buttonDeletar = view.findViewById<Button>(R.id.btnDeletar)
        val buttonCriarComentario = view.findViewById<Button>(R.id.enviarComentario)
        val imagemModal = view.findViewById<ImageView>(R.id.fotoModal)
        val usuarioPostagem: TextView = view.findViewById(R.id.UsuarioPostagem)
        val imageUrl = arguments?.getString("image_url")
        comentariosArtificiais = mutableListOf<Comentario>()
        textoComentario =  view.findViewById(R.id.textComment)

        if (post.comentarios != null) {
            fun realizarValidacoes(comentario: Comentario) {
                comentariosArtificiais.add(
                    Comentario(
                        usuario = comentario.usuario,
                        comentarioTexto = comentario.comentarioTexto,
                        criadoEm = comentario.criadoEm,
                        atualizadoEm = comentario.atualizadoEm,
                        _id = comentario._id
                    )
                )
            }

            post.comentarios.forEach { post -> realizarValidacoes(post) }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.RvComentarios)
        adapterComentarios = AdapterComentarios(requireContext(), comentariosArtificiais)
        val espacamento = resources.getDimensionPixelSize(R.dimen.espacamento_entre_itens)

        recyclerView.addItemDecoration(SpaceItemDecoration(espacamento))

        curtidas = view.findViewById(R.id.numCurtidas)
        addSeguindo = view.findViewById(R.id.addSeguindo)
        iconHeart = view.findViewById(R.id.iconHeart)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.adapter = adapterComentarios


        curtidas.text = post.curtidas.size.toString()
        if (post.curtidas.contains(nomeUsuarioLogado)) {
            iconHeart.setColorFilter(Color.parseColor("#FF4081"))
            iconHeart.setOnClickListener {curtirFoto(true)}
        } else {
            iconHeart.setOnClickListener {curtirFoto(false)}
        }

        if (usuario.seguindo.contains(post.usuario)) {
            addSeguindo.setColorFilter(Color.parseColor("#FF4081"))
            addSeguindo.setOnClickListener{Seguir(true)}
        } else {
            addSeguindo.setOnClickListener{Seguir(false)}
        }


        buttonDeletar.visibility = if (!buttonActive) View.GONE else View.VISIBLE
        usuarioPostagem.visibility = if (buttonActive) View.GONE else View.VISIBLE
        addSeguindo.visibility = if (buttonActive) View.GONE else View.VISIBLE
        usuarioPostagem.text = post.usuario

        if (buttonActive) {
            buttonDeletar.setOnClickListener {deletarPost()}
        }

        buttonCriarComentario.setOnClickListener {criarComentario()}

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .into(imagemModal)
        }

        return view
    }

    private fun Seguir(seguindo: Boolean) {
        val retrofitCli: RetrofitClient = RetrofitClient()
        val call = retrofitCli.servicoUsuario.seguir(nomeUsuarioLogado, post.usuario, "Bearer ${token_usuario}")
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, infosResponse: Response<Void>) {
                if (!infosResponse.isSuccessful) {
                    Log.e("Seguiu!", "etr: " + infosResponse)
                    Toast.makeText(requireContext(), "Erro ao seguir!", Toast.LENGTH_LONG).show()
                    return
                }

                if (seguindo) {
                    val seguindoMutavel = usuario.seguindo.toMutableList()
                    seguindoMutavel.remove(post.usuario)
                    val novaListaSeguindo = seguindoMutavel.toList()
                    usuario.seguindo = novaListaSeguindo.toTypedArray()

                    Log.e("Seguiu!", "Parou de Seguir!")
                    addSeguindo.setColorFilter(Color.parseColor("#000000"))
                    addSeguindo.setOnClickListener {Seguir(false)}
                    Toast.makeText(requireContext(), "Você parou de seguir!", Toast.LENGTH_LONG).show()
                    fragment?.onFollowActionCompleted()
                    return;
                }

                val novaListaSeguindo = usuario.seguindo + post.usuario
                usuario.seguindo = novaListaSeguindo

                Log.e("Seguiu!", "Começou a seguir!")

                Toast.makeText(requireContext(), "Você começou a seguir!", Toast.LENGTH_LONG).show()
                addSeguindo.setColorFilter(Color.parseColor("#FF4081"))
                addSeguindo.setOnClickListener {Seguir(true)}
                fragment?.onFollowActionCompleted()
            }


            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Seguiu!", "onFailure: " + t)
                Toast.makeText(requireContext(), "Erro ao seguir!", Toast.LENGTH_LONG).show()
                return
            }
        })
    }

    override fun onResume() {
        super.onResume()

        val width = (resources.displayMetrics.widthPixels * 1.00).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.95).toInt()

        dialog?.window?.setLayout(width, height)
    }

    private fun deletarPost() {
        val retrofitCli: RetrofitClient = RetrofitClient()
        val call = retrofitCli.servicoUsuario.deletePost(post._id, "Bearer ${token_usuario}")
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, infosResponse: Response<Void>) {
                if (!infosResponse.isSuccessful) {
                    Log.e("Erro Desconhecido! 4", "etr: " + infosResponse)
                    Toast.makeText(requireContext(), "Falha ao Deletar POST", Toast.LENGTH_LONG).show()
                    return
                } else {
                    Log.e("Post Deletado", "Deletado" )
                    Toast.makeText(requireContext(), "POST DELETADO!", Toast.LENGTH_LONG).show()
                    onPostInteractionListener.onPostDeleted(post)

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Falha ao deletar post", "onFailure: " + t)
                Toast.makeText(requireContext(), "Falha ao deletar post!", Toast.LENGTH_LONG).show()
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
                    Toast.makeText(requireContext(), "Erro ao deixar/tirar o like!", Toast.LENGTH_LONG).show()
                    return
                }

                if (like) {
                    val curtidasMutavel = post.curtidas.toMutableList()
                    curtidasMutavel.remove(nomeUsuarioLogado)
                    val novaListaCurtidas = curtidasMutavel.toList()
                    post.curtidas = novaListaCurtidas

                    Log.e("deslike!", "deslikeou!")
                    curtidas.text = post.curtidas.size.toString()
                    iconHeart.setColorFilter(Color.parseColor("#000000"))
                    iconHeart.setOnClickListener {curtirFoto(false)}
                    Toast.makeText(requireContext(), "Você tirou seu Like!", Toast.LENGTH_LONG).show()
                    return;
                }

                val novaListaCurtidas = post.curtidas + nomeUsuarioLogado
                post.curtidas = novaListaCurtidas
                Log.e("like!", "Curtioooooo!")

                Toast.makeText(requireContext(), "Você deixou seu Like!", Toast.LENGTH_LONG).show()
                curtidas.text = post.curtidas.size.toString()
                iconHeart.setColorFilter(Color.parseColor("#FF4081"))
                iconHeart.setOnClickListener {curtirFoto(true)}
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Falha ao dar like/deslike", "onFailure: " + t)
                Toast.makeText(requireContext(), "Erro ao deixar/tirar o like!", Toast.LENGTH_LONG).show()
                return
            }
        })
    }

    private fun criarComentario() {
        if (textoComentario.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Você deve preencher o campo de comentario", Toast.LENGTH_LONG).show()
            return
        }

        val retrofitCli: RetrofitClient = RetrofitClient()
        val call = retrofitCli.servicoUsuario.createComment(post._id, "Bearer ${token_usuario}", mapOf(
            "usuario" to nomeUsuarioLogado,
            "comentarioTexto" to textoComentario.text.toString(),
        ))

        call.enqueue(object : Callback<JsonObject> {
          override fun onResponse(call: Call<JsonObject>, criarPost: Response<JsonObject>) {
                if (!criarPost.isSuccessful) {
                    Log.e("Criar Comentario", "Erro ao Criar Comentario: " + criarPost)
                    Toast.makeText(requireContext(), "Erro ao Criar Comentario", Toast.LENGTH_LONG).show()
                    return
                }

                val criarPostBody = criarPost.body()
                if (criarPostBody != null) {
                    Log.e("Criar Comentario",  "Comentario Criado: ${criarPostBody}")
                    Toast.makeText(requireContext(), "Comentario Criado!", Toast.LENGTH_LONG).show()
                    val newComment = Comentario(
                        usuario = nomeUsuarioLogado,
                        comentarioTexto = textoComentario.text.toString(),
                        criadoEm = "",
                        atualizadoEm = "",
                        _id = ""
                    )

                    comentariosArtificiais.add(newComment)
                    post.comentarios = (post.comentarios ?: emptyList()).toMutableList().apply { add(newComment) }.toList()

                    textoComentario.text = Editable.Factory.getInstance().newEditable("")
                    adapterComentarios.notifyDataSetChanged()
                    return
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(requireContext(), "Erro ao Criar Comentario", Toast.LENGTH_LONG).show()
                Log.e("Criar Comentario", "Erro ao criar Comentario: " + t)
                return
            }
        })
    }


    class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) {
                outRect.bottom = space
            }
        }
    }

}






