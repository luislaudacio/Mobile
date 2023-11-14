package com.example.projetointegrador.adapters

import com.example.projetointegrador.models.Post

interface OnPostInteractionListener {
    fun onPostDeleted(post: Post)
}