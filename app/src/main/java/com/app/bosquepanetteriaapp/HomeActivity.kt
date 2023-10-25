package com.app.bosquepanetteriaapp

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
        window.statusBarColor = Color.TRANSPARENT

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.btnv)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.menu.findItem(R.id.menu_home).isChecked = true

        val btnMenuHome = findViewById<ImageView>(R.id.btn_menu_home)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

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

    fun openCartActivity(view: View) {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

    private inner class FetchProductDataTask(private val recyclerView: RecyclerView) :
        AsyncTask<Void, Void, List<Product>>() {

        override fun doInBackground(vararg params: Void?): List<Product> {
            val apiUrl =
                "https://windy-energy-396600.rj.r.appspot.com/produtos" // Substitua pela URL da sua API
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(apiUrl)
                .build()

            val products = mutableListOf<Product>()

            try {
                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseData: String = response.body?.string() ?: ""
                    val jsonArray = JSONArray(responseData)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val prod_id = jsonObject.getInt("prod_id")
                        val prod_nome = jsonObject.getString("prod_nome")
                        val prod_imagem = jsonObject.getString("prod_imagem")
                        val prod_preco = jsonObject.getDouble("prod_preco")
                        val prod_descricao = jsonObject.getString("prod_descricao")

                        val product =
                            Product(prod_id, prod_nome, prod_imagem, prod_preco, prod_descricao)
                        products.add(product)
                    }
                } else {
                    // Tratar erro de resposta da API, se necessário
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Tratar erro de rede ou parsing JSON, se necessário
            }

            return products
        }

        override fun onPostExecute(result: List<Product>?) {
            super.onPostExecute(result)

            // Atualize a interface do usuário com os resultados após a conclusão da tarefa em segundo plano
            if (result != null) {
                val adapter = ProductAdapter(result) { productId ->
                    val intent = Intent(this@HomeActivity, ProductDetailActivity::class.java)
                    intent.putExtra(
                        "productId",
                        productId
                    ) // Substitua 'productId' pelo ID do produto que você deseja passar
                    intent.putParcelableArrayListExtra("productList", ArrayList(result))
                    startActivity(intent)
                }
                recyclerView.adapter = adapter
            }
        }
    }
}
