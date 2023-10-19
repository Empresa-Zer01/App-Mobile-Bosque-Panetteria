package com.app.bosquepanetteriaapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException
import android.util.Log
import android.widget.TextView

class CadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cad)

        window.statusBarColor = Color.TRANSPARENT
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        title = ""

        val btnEnt: TextView = findViewById(R.id.btnentreaqui)

        btnEnt.setOnClickListener {
            val intent = Intent(this, LogActivity::class.java)
            startActivity(intent)
        }

        val btnCadastrar: Button = findViewById(R.id.btn_cadastrar)

        btnCadastrar.setOnClickListener {
            val usu_nome = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editNome).text.toString()
            val usu_email = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editEmail).text.toString()
            val usu_telefone = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editTelefone).text.toString()
            val usu_senha = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editSenha).text.toString()
            val usu_confirmar_senha = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editConfirmarSenha).text.toString()


            // Verifique se os campos obrigatórios estão vazios
            if (usu_nome.isEmpty() || usu_email.isEmpty() || usu_telefone.isEmpty() || usu_senha.isEmpty()) {
                Toast.makeText(this@CadActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(usu_email)) {
                Toast.makeText(this@CadActivity, "O email inserido não é válido", Toast.LENGTH_SHORT).show()
            } else if (!isValidPhoneNumber(usu_telefone)) {
                Toast.makeText(this@CadActivity, "O número de telefone inserido não é válido", Toast.LENGTH_SHORT).show()
            } else if (usu_senha != usu_confirmar_senha) {
                Toast.makeText(this@CadActivity, "As senhas não correspondem", Toast.LENGTH_SHORT).show()
            } else {
                // Crie um objeto JSON com os dados capturados
                val json = JSONObject().apply {
                    put("usu_nome", usu_nome)
                    put("usu_email", usu_email)
                    put("usu_telefone", usu_telefone)
                    put("usu_senha", usu_senha)
                }

            // Crie uma solicitação HTTP POST para a API Flask
            val client = OkHttpClient()
            val url = "https://windy-energy-396600.rj.r.appspot.com/usuarios"
            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            // Execute a solicitação
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@CadActivity, "Erro ao enviar solicitação", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()

                        Log.d("Resposta da API", responseBody ?: "Resposta vazia")

                        runOnUiThread {
                            Toast.makeText(this@CadActivity, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@CadActivity, HomeActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@CadActivity, "Erro na solicitação: ${response.code}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
        }
    }
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
        return email.matches(emailRegex.toRegex())
    }
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phonePattern = "\\d{11}"  // Verifica se o telefone possui 11 dígitos
        return phoneNumber.matches(phonePattern.toRegex())
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}