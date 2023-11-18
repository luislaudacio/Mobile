package com.example.projetointegrador.services

import com.example.projetointegrador.models.Post

interface OnPostUploadedListener {
    fun onPostUploaded(newPost:Post)
}