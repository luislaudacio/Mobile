package com.example.projetointegrador.dao

import com.example.projetointegrador.models.Usuario
import kotlin.collections.ArrayList

public class DaoUsuarios {
    var listaUsuarios:ArrayList<Usuario> = ArrayList();

    public fun getLista():ArrayList<Usuario> {
        return listaUsuarios
    }

    public fun CadastraUsuario(usuario:Usuario) {
        listaUsuarios.add(usuario)
    }

    public fun AtualizaUsuario(usuario: Usuario, posicao:Int) {
        listaUsuarios.set(posicao, usuario)
    }

    public fun RemoveContato(posicao: Int) {
        listaUsuarios.removeAt(posicao)
    }
}