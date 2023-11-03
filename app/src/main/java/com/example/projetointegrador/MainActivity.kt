package com.example.projetointegrador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.projetointegrador.api.RetrofitClient
import com.example.projetointegrador.models.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    lateinit var usuario:Usuario

    lateinit var btnLogar:Button
    lateinit var txtEmail:EditText
    lateinit var txtSenha:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btnLogar = findViewById(R.id.btnSalvar)
        txtEmail = findViewById(R.id.txtEmail)
        txtSenha = findViewById(R.id.txtSenha)


        btnLogar.setOnClickListener {logar()}
    }

    fun criarConta(view: View) {
        var intent: Intent = Intent(this, TelaCadastro:: class.java)
        startActivity(intent)
    }
    private fun logar () {
        var intent: Intent = Intent(this, MinhaConta:: class.java)
        startActivity(intent)



//        if(txtEmail.text.toString().isNullOrEmpty() || txtSenha.text.toString().isNullOrEmpty()) {
//            Toast.makeText(this@MainActivity,
//                "Dados Não informados.", Toast.LENGTH_LONG).show()
//
//            return;
//        }
//
//        usuario = Usuario(
//            access_token = "",
//            email = txtEmail.text.toString(),
//            senha = txtSenha.text.toString()
//        )
//
//        val retrofitCli: RetrofitClient = RetrofitClient()
//        retrofitCli.servicoUsuario.getToken(usuario)
//            .enqueue(object : Callback<Usuario> {
//
//                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
//                    if (!response.isSuccessful) {
//                        Log.e("Usuario ou senha Incorretos", "response: " + response)
//                        Toast.makeText(this@MainActivity, "Usuario ou senha Incorretos", Toast.LENGTH_LONG).show()
//                        return
//                    }
//
//                    val jsonResponse = response.body()
//                    if (jsonResponse != null) {
//                        usuario.access_token = jsonResponse.access_token
//                        Log.i("Token Gerado", "onResponse: " + jsonResponse.toString())
//                        Toast.makeText(this@MainActivity, "Token Gerado", Toast.LENGTH_LONG).show()
//
////                        var intent: Intent = Intent(this@MainActivity, TelaCadastro:: class.java)
////                        intent.putExtra("usuario", usuario)
////                        startActivity(intent)
////                        return
////                        val extras = intent.extras
////                        if (extras != null) {
////                            usuario = getIntent().getSerializableExtra("usuario") as Usuario
////                            Log.i("testad", usuario.toString())
////                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<Usuario>, t: Throwable) {
//                    Log.e("Falha ao criar token", "onFailure: " + t)
//                    Toast.makeText(this@MainActivity, "Falha ao criar token", Toast.LENGTH_LONG).show()
//                    return
//                }
//            })




    }
}

