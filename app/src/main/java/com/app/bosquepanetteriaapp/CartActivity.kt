package com.app.bosquepanetteriaapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartActivity : AppCompatActivity() {
    private lateinit var adapter: CartItemAdapter
    private lateinit var totalTextView: TextView
    private lateinit var checkoutButton: Button
    private var cartItems = mutableListOf<CartItem>()

    companion object {
        private var instance: CartActivity? = null

        fun getInstance(): CartActivity? {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_cart)
        window.statusBarColor = Color.WHITE

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.btnv)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.menu.findItem(R.id.menu_carrinho).isChecked = true

        // Armazene a instância única da CartActivity
        instance = this

        // Verifique se a lista de itens do carrinho já existe na atividade
        if (cartItems.isEmpty()) {
            // Obtenha a lista de itens do carrinho da intent
            val cartItemsFromIntent = intent.getParcelableArrayListExtra<CartItem>("cartItems")

            // Verifique se há itens na lista da intent e adicione-os aos itens do carrinho
            if (cartItemsFromIntent != null) {
                cartItems.addAll(cartItemsFromIntent)
            }
        }

        // Obtenha referências para os elementos de interface do usuário
        val cartListView: ListView = findViewById(R.id.cartListView)
        totalTextView = findViewById(R.id.total_txtview)
        checkoutButton = findViewById(R.id.checkoutButton)

        // Configure o adaptador para a lista de itens do carrinho
        adapter = CartItemAdapter(this, R.layout.cart_item_layout, cartItems) { item ->
            // Chame a função de remoção de item da CartActivity aqui
            removeCartItem(item)

        }
        cartListView.adapter = adapter

        // Configure um ouvinte de clique para o botão de finalização da compra
        checkoutButton.setOnClickListener {
            if (cartItems.isNotEmpty()) { // Verifique se o carrinho não está vazio
                // Implemente aqui a lógica para finalizar a compra (por exemplo, pagamento, envio, etc.)
                // Após a finalização da compra, você pode esvaziar o carrinho
                cartItems.clear()
                adapter.notifyDataSetChanged()
                updateTotal()
            }
        }

        // Configure um ouvinte de clique para os itens do carrinho (remoção)
        cartListView.setOnItemClickListener { _, _, position, _ ->
            removeItem(position)
        }

        // Atualize o total inicial
        updateTotal()
    }

    private fun removeItem(position: Int) {
        cartItems.removeAt(position)
        adapter.notifyDataSetChanged()
        updateTotal()
    }

    private fun removeCartItem(item: CartItem) {
        cartItems.remove(item)
        adapter.notifyDataSetChanged()
        updateTotal()
    }

    private fun updateTotal() {
        var total = 0.0
        for (item in cartItems) {
            total += item.productPrice * item.quantity
        }
        totalTextView.text = String.format("Total: R$ %.2f", total)
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
