package com.app.bosquepanetteriaapp

import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar se o usuário está autenticado usando SharedPreferences
        val preferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        val isLoggedIn = preferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Se o usuário estiver autenticado, vá para a HomeActivity
            startActivity(Intent(this, HomeActivity::class.java))
            finish() // Feche a atividade atual para que o usuário não possa voltar
            return // Evite a execução adicional do código abaixo se já estivermos indo para a HomeActivity
        }

        // Se o usuário não estiver autenticado, continue com o código normal

        setContentView(R.layout.activity_main)

        window.statusBarColor = Color.WHITE

        val btnCad: Button = findViewById(R.id.btn_cad)
        val btnLog: Button = findViewById(R.id.btn_log)

        btnCad.setOnClickListener {
            val intent = Intent(this, CadActivity::class.java)
            startActivity(intent)
        }

        btnLog.setOnClickListener {
            val intent = Intent(this, LogActivity::class.java)
            startActivity(intent)
        }
    }

}
