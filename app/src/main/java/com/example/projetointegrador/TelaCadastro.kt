package com.example.projetointegrador

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetointegrador.api.RetrofitClient
import com.example.projetointegrador.models.Usuario
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TelaCadastro : AppCompatActivity() {

    lateinit var usuario: Usuario
    lateinit var txtUsuario:EditText
    lateinit var txtEmail:EditText
    lateinit var txtData:EditText
    lateinit var txtSenha:EditText
    lateinit var btnSalvar:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_cadastro)

        Locale.setDefault(Locale("pt", "BR"))

        txtUsuario = findViewById(R.id.txtUsuario)
        txtEmail = findViewById(R.id.txtEmail)
        txtData = findViewById(R.id.txtData)
        txtSenha = findViewById(R.id.txtSenha)
        btnSalvar = findViewById(R.id.btnSalvar)



        txtData.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val dataSelecionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    txtData.setText(dataSelecionada)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            val dataMinima = Calendar.getInstance()
            dataMinima.set(1900, 1, 1)
            datePicker.datePicker.minDate = dataMinima.timeInMillis



            val dataMaxima = System.currentTimeMillis()
            datePicker.datePicker.maxDate = dataMaxima

            datePicker.show()
        }

        btnSalvar.setOnClickListener { cadastrar () }
    }


    fun logarConta(view: View) {
        var intent: Intent = Intent(this, MainActivity:: class.java)
        startActivity(intent)
    }

    private fun cadastrar () {
        if(txtEmail.text.toString().isNullOrEmpty() || txtSenha.text.toString().isNullOrEmpty() || txtUsuario.text.toString().isNullOrEmpty() || txtData.text.toString().isNullOrEmpty()) {
            Toast.makeText(this@TelaCadastro,
                "Dados Não informados.", Toast.LENGTH_LONG).show()

            return;
        }

        if (!txtEmail.text.toString().contains(Regex(".*@gmail\\.com$"))) {
            Toast.makeText(this@TelaCadastro,
                "Email precisa ser um gmail.", Toast.LENGTH_LONG).show()

            return;
        }

        if (txtUsuario.text.toString().length < 5) {
            Toast.makeText(this@TelaCadastro,
                "Usuario precisa tem no minimo 6 caracteres.", Toast.LENGTH_LONG).show()

            return;
        }

//        val temCaractereEspecial = txtSenha.text.toString().contains(Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\",.<>?]"))
//        val temLetraMaiuscula = txtSenha.text.toString().contains(Regex("[A-Z]"))
//        val temLetraMinuscula = txtSenha.text.toString().contains(Regex("[a-z]"))  temCaractereEspecial && temLetraMaiuscula && temLetraMinuscula
        val temDoisNumeros = txtSenha.text.toString().count { it.isDigit() } >= 2
        val temPeloMenosCincoCaracteres = txtSenha.text.toString().length >= 5

        val senhaValida = temDoisNumeros && temPeloMenosCincoCaracteres

        if (!senhaValida) {
            Toast.makeText(this@TelaCadastro,
                "Sua Senha é muito fraca, 2 numeros e no minimo 5 digitos", Toast.LENGTH_LONG).show()

            return;
        }

        val retrofitCli: RetrofitClient = RetrofitClient()
        retrofitCli.servicoUsuario.userRegister(mapOf(
            "email" to txtEmail.text.toString(),
            "senha" to txtSenha.text.toString(),
            "usuario" to txtUsuario.text.toString(),
            "dataNasc" to formatarData(txtData.text.toString()),
        )).enqueue(object : Callback<Usuario> {

                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (!response.isSuccessful) {
                        val errorResponse = response.errorBody()?.string() ?: ""
                        val errorJsonObject = Json.parseToJsonElement(errorResponse) as JsonObject
                        val errorMessage = errorJsonObject["message"].toString()

                        if (errorResponse != null) {
                            Log.e("Dados Invalidos", "errorResponse: " + errorMessage)
                            Toast.makeText(this@TelaCadastro, "Dados Invalidos: ${errorMessage.toString()}", Toast.LENGTH_LONG).show()
                            return
                        }

                        Log.e("Dados Invalidos", "response: " + response)
                        Toast.makeText(this@TelaCadastro, "Dados Invalidos", Toast.LENGTH_LONG).show()
                        return
                    }

                    val jsonResponse = response.body()
                    if (jsonResponse != null) {
                        Log.i("Usuario Criado", "onResponse: " + jsonResponse.toString())
                        Toast.makeText(this@TelaCadastro, "Usuario Criado", Toast.LENGTH_LONG).show()

                        var intent: Intent = Intent(this@TelaCadastro, MainActivity:: class.java)
                        intent.putExtra("usuario", usuario)
                        startActivity(intent)
                        return
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Log.e("Falha ao criar token", "onFailure: " + t)
                    Toast.makeText(this@TelaCadastro, "Falha ao criar token", Toast.LENGTH_LONG).show()
                    return
                }
            })
    }

    fun formatarData(data: String): String {
        val formatoEntrada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatoSaida = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val dataFormatada: Date = formatoEntrada.parse(data) ?: Date()
        return formatoSaida.format(dataFormatada)
    }

}