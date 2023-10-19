package com.app.bosquepanetteriaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import com.google.android.material.bottomnavigation.BottomNavigationView

class PedidosActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_pedidos)
        window.statusBarColor = Color.WHITE

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.btnv)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.menu.findItem(R.id.menu_pedidos).isChecked = true
    }
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_cardapio -> {
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