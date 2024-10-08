package com.example.projetointegrador.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projetointegrador.ModalPostagemFragment
import com.example.projetointegrador.R
import com.example.projetointegrador.SeguindoFragment
import com.example.projetointegrador.models.Post
import com.example.projetointegrador.models.Usuario
import com.example.projetointegrador.models.modalItem
import com.example.projetointegrador.services.OnPostInteractionListener

class AdapterFragmentSG(private val context: Context, private var listaImagens: MutableList<modalItem>, private var usuario: Usuario, private var fragmentSG: SeguindoFragment) : RecyclerView.Adapter<AdapterFragmentSG.MeuViewHolder> (),
    OnPostInteractionListener {
    lateinit var modalFragment: ModalPostagemFragment
    override fun onPostDeleted(post: Post) {
        removerItem(post)
        dismissModal()
    }
    class MeuViewHolder(itemView: View, val contexto: Context) : RecyclerView.ViewHolder(itemView) {

        val estiloImagem: ImageView = itemView.findViewById(R.id.estiloImagem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeuViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view = inflater.inflate(R.layout.style_imagens, parent, false)

        return MeuViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MeuViewHolder, position: Int) {
        val imageItem = listaImagens[position].image
        val buttonActive = listaImagens[position].nomeUsuario == listaImagens[position].Post.usuario;

        Glide.with(context)
            .load(imageItem.imageUrl)
            .into(holder.estiloImagem)

        holder.estiloImagem.setOnClickListener{
            modalFragment = ModalPostagemFragment(buttonActive, usuario, listaImagens[position].Post, listaImagens[position].nomeUsuario, listaImagens[position].tokenUsuario, this, fragmentSG)
            val args = Bundle()
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager

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
            listaImagens.remove(item)
            notifyDataSetChanged()
        }
    }





}