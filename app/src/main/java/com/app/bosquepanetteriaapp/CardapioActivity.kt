package com.app.bosquepanetteriaapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CardapioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_cardapio)
        window.statusBarColor = Color.WHITE

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.btnv)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.menu.findItem(R.id.menu_cardapio).isChecked = true

    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_cardapio -> {
                    // Remova esta linha se não quiser reiniciar a mesma atividade
                    startActivity(Intent(this, CardapioActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_pedidos -> {
                    startActivity(Intent(this, PedidosActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_carrinho -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}