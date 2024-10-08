package com.example.projetointegrador.NavigationUtils

import android.content.Context
import android.content.Intent
import com.example.projetointegrador.MainActivity

object Navigation {
    fun redirectToMainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}