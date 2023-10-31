package com.example.projetointegrador

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class TelaCadastro : AppCompatActivity() {

    lateinit var txtUsuario:EditText
    lateinit var txtEmail:EditText
    lateinit var txtData:EditText
    lateinit var txtSenha:EditText
    lateinit var btnSalvar:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_cadastro)

        txtUsuario = findViewById(R.id.txtUsuario)
        txtEmail = findViewById(R.id.txtEmail)
        txtData = findViewById(R.id.txtData)
        txtSenha = findViewById(R.id.txtSenha)
        btnSalvar = findViewById(R.id.btnSalvar)



    }
    fun logarConta(view: View) {
        var intent: Intent = Intent(this, MainActivity:: class.java)
        startActivity(intent)
    }
}