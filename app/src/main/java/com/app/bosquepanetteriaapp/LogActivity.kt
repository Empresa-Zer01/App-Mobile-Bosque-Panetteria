package com.app.bosquepanetteriaapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class LogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        // Verificar se o usuário já está logado
        val preferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        val isLoggedIn = preferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            showLoading()
            // Se estiver logado, vá diretamente para a HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()  // Finalize a atividade atual para que o usuário não possa voltar à tela de login pressionando "Voltar"
        } else {
            // Continue com o código normal de criação da atividade
            // ...

            val btnLog: Button = findViewById(R.id.btn_entrar)

            btnLog.setOnClickListener {
                val usu_email =
                    findViewById<TextInputEditText>(R.id.editEmail).text.toString()
                val usu_senha =
                    findViewById<TextInputEditText>(R.id.editSenha).text.toString()

                if (usu_email.isEmpty() || usu_senha.isEmpty()) {
                    Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                } else {
                    // Criar um objeto JSON com os dados de login
                    val json = JSONObject().apply {
                        put("usu_email", usu_email)
                        put("usu_senha", usu_senha)
                    }

                    // Criar uma solicitação HTTP POST para autenticar o usuário
                    val client = OkHttpClient()
                    val url =
                        "https://windy-energy-396600.rj.r.appspot.com/login" // Substitua pelo URL da sua API Flask para autenticação
                    val requestBody =
                        RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
                    val request = Request.Builder().url(url).post(requestBody).build()

                    // Executar a solicitação
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            runOnUiThread {
                                hideLoading()  // Esconder a tela de carregamento em caso de falha
                                Toast.makeText(
                                    this@LogActivity, "Erro ao enviar solicitação", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                val responseBody = response.body?.string()
                                Log.d("Resposta da API", responseBody ?: "Resposta vazia")

                                // Verificar se a resposta indica uma autenticação bem-sucedida
                                // Você pode analisar a resposta para determinar isso

                                runOnUiThread {
                                    hideLoading()  // Esconder a tela de carregamento em caso de sucesso
                                    Toast.makeText(
                                        this@LogActivity, "Login bem-sucedido", Toast.LENGTH_SHORT
                                    ).show()

                                    // Quando o login é bem-sucedido, salve o status no SharedPreferences
                                    val editor = preferences.edit()
                                    editor.putBoolean("isLoggedIn", true)
                                    editor.apply()

                                    // Vá para a HomeActivity
                                    val intent = Intent(this@LogActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                runOnUiThread {
                                    hideLoading()  // Esconder a tela de carregamento em caso de falha
                                    Toast.makeText(
                                        this@LogActivity, "Credenciais inválidas", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    })
                }
            }

            val btnForgPass: TextView = findViewById(R.id.btnforgpass)

            btnForgPass.setOnClickListener {
                val intent = Intent(this, ForgPassActivity::class.java)
                startActivity(intent)
                toggleTextColor(btnForgPass)
            }

            val btnReg: TextView = findViewById(R.id.btnRegistreSe)

            btnReg.setOnClickListener {
                val intent = Intent(this, CadActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun toggleTextColor(textView: TextView) {
        val colorStateList =
            ContextCompat.getColorStateList(this, R.color.text_color_selector)

        textView.setTextColor(colorStateList)
    }

    private fun showLoading() {
        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)
    }

    private fun hideLoading() {
        // Adicione lógica aqui para esconder a tela de carregamento, se necessário
        // Por exemplo, pode ser finish() da LoadingActivity após um tempo ou com base em algum evento de conclusão.
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}