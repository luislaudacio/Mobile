package com.example.projetointegrador.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projetointegrador.MinhaConta
import com.example.projetointegrador.ModalPostagemFragment
import com.example.projetointegrador.R
import com.example.projetointegrador.api.RetrofitClient
import com.example.projetointegrador.models.ImageItem
import com.example.projetointegrador.models.Post
import com.example.projetointegrador.models.Usuario
import com.example.projetointegrador.models.modalItem
import com.example.projetointegrador.services.OnPostInteractionListener
import com.example.projetointegrador.services.OnPostUploadedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdapterMConta(
    var usuario: Usuario,
    var contexto: MinhaConta,
    private var listaImagens:MutableList<modalItem>,
    private var recyclerView: RecyclerView
):
    RecyclerView.Adapter<AdapterMConta.MeuViewHolder> () ,
    OnPostInteractionListener, OnPostUploadedListener {
    lateinit var modalFragment: ModalPostagemFragment

    override fun onPostDeleted(post: Post) {
        removerItem(post)
        dismissModal()
    }

    override fun onPostUploaded(newPost: Post) {
        adicionarItem(this, newPost)
    }

    class MeuViewHolder(itemView: View, val contexto: Context) : RecyclerView.ViewHolder(itemView) {

        val estiloImagem: ImageView = itemView.findViewById(R.id.estiloImagem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeuViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(contexto)
        var view = inflater.inflate(R.layout.style_imagens, parent, false)

        return MeuViewHolder(view, contexto)
    }

    override fun onBindViewHolder(holder: MeuViewHolder, position: Int) {
        val imageItem = listaImagens[position].image
        val buttonActive = listaImagens[position].nomeUsuario == listaImagens[position].Post.usuario;

        Glide.with(contexto)
            .load(imageItem.imageUrl)
            .into(holder.estiloImagem)


        holder.estiloImagem.setOnClickListener{
            modalFragment = ModalPostagemFragment(buttonActive, listaImagens[position].Post, listaImagens[position].nomeUsuario, listaImagens[position].tokenUsuario, this)
            val args = Bundle()
            val fragmentManager = (contexto as AppCompatActivity).supportFragmentManager

            args.putString("image_url", imageItem.imageUrl)  // Passe a URL da imagem como argumento
            modalFragment.arguments = args

            modalFragment.show(fragmentManager, "MEU_MODAL")
        }


    }

    override fun getItemCount(): Int {
        return this.listaImagens.size
    }

    private fun dismissModal() {
        modalFragment?.dismiss()
    }

    private fun removerItem(post: Post) {
        val item = listaImagens.firstOrNull { it.Post == post }

        if (item != null) {
            usuario.posts?.toMutableList()?.apply { removeAll { it == post } }
            listaImagens.remove(item)
            notifyDataSetChanged()
        }
    }

    private fun adicionarItem(adapter: AdapterMConta,post: Post) {
        val item = modalItem(
            image = ImageItem(post.pathFotoPost),
            nomeUsuario = usuario.usuario,
            tokenUsuario = usuario.access_token,
            Post = post
        )

        listaImagens.add(item)
        usuario.posts = (usuario.posts?: emptyList()).toMutableList().apply { add(post) }.toList()

        atualizarPostsNaAPI(
            onSuccess = { posts ->
                if (posts != null) {
                    usuario.posts = posts
                }

                listaImagens = mutableListOf<modalItem>()
                if (usuario.posts != null) {
                    usuario.posts.forEach { post ->
                        listaImagens.add(modalItem(
                            image = ImageItem(post.pathFotoPost),
                            nomeUsuario = usuario.usuario,
                            tokenUsuario = usuario.access_token,
                            Post = post
                        ))
                    }
                }

                contexto.runOnUiThread {
                    notifyDataSetChanged()
                }
            },
            onFailure =  { t ->
                Log.e("Erro na atualização", "onFailure: " + t)
                Toast.makeText(contexto, "Erro ao buscar posts", Toast.LENGTH_LONG).show()
            }
        )
    }

    fun atualizarListaImagens(novaListaImagens: List<modalItem>) {
        listaImagens.clear()
        listaImagens.addAll(novaListaImagens)
    }
    fun atualizarPostsNaAPI(
        onSuccess: (List<Post>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val retrofitCli: RetrofitClient = RetrofitClient()
        val call = retrofitCli.servicoUsuario.getUserInfo(usuario.email, "Bearer ${usuario.access_token}")
        call.enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, infosResponse: Response<Usuario>) {
                if (!infosResponse.isSuccessful) {
                    Log.e("Erro Desconhecido!", "response: " + infosResponse)
                    onFailure(Throwable("Erro desconhecido"))
                    return
                }

                val infoResponse = infosResponse.body()
                if (infoResponse != null) {
                    Log.i("Informacoes Usuario", "onResponse: " + infoResponse.toString())
                    usuario.criadoEm = infoResponse.criadoEm
                    usuario.usuario = infoResponse.usuario
                    usuario.dataNasc = infoResponse.dataNasc
                    usuario.seguidores = infoResponse.seguidores
                    usuario.seguindo = infoResponse.seguindo

                    if (infoResponse.posts != null) {
                        usuario.posts = infoResponse.posts
                        onSuccess(usuario.posts)
                    }
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Log.e("Falha ao buscar info", "onFailure: " + t)
                onFailure(t)
            }
        })
    }
}
