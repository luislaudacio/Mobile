package com.example.projetointegrador

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.Format
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TelaCadastro : AppCompatActivity() {

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

    }


    fun logarConta(view: View) {
        var intent: Intent = Intent(this, MainActivity:: class.java)
        startActivity(intent)
    }

}