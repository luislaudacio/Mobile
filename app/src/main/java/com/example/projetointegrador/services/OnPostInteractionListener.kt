package com.example.projetointegrador.services

import com.example.projetointegrador.models.Post

interface OnPostInteractionListener {
    fun onPostDeleted(post: Post)
}